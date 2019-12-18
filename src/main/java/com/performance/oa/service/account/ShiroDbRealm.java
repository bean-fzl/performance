/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.performance.oa.service.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Objects;
import com.performance.commons.utils.StringUtils;
import com.performance.oa.entity.Employe;
import com.performance.oa.entity.Properties;
import com.performance.oa.service.employe.EmployeService;
import com.performance.oa.service.properties.PropertiesService;

public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private EmployeService employeService;
    @Autowired
    private PropertiesService propertiesService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        Employe e = employeService.findByLoginName(token.getUsername());
        if (e != null) {
            return new SimpleAuthenticationInfo(new ShiroUser(e.getId(), e.getLoginName(), e.getName()),
                    e.getPassword(), getName());
        } else {
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        Employe e = employeService.findByLoginName(shiroUser.loginName);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> list = new ArrayList<>();
        if (e.getIsMt()) {
            list.add("mt");
        }
        if (e.getIsPm()) {
            list.add("pm");
        }
        if (list.isEmpty()) {
            list.add("employe");
        }
        Properties p = propertiesService.findByName("hr_username");
        if (p != null && StringUtils.isNotEmpty(p.getValue())) {
            if (Arrays.asList(p.getValue().split(",")).contains(shiroUser.loginName)) {
                list.add("hr");
            }
        }
        info.addRoles(list);
        return info;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        //        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
        //        matcher.setHashIterations(AccountService.HASH_INTERATIONS);
        //
        //        setCredentialsMatcher(matcher);
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        public Long id;
        public String loginName;
        public String name;

        public ShiroUser(Long id, String loginName, String name) {
            this.id = id;
            this.loginName = loginName;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return loginName;
        }

        /**
         * 重载hashCode,只计算loginName;
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(loginName);
        }

        /**
         * 重载equals,只计算loginName;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (loginName == null) {
                if (other.loginName != null) {
                    return false;
                }
            } else if (!loginName.equals(other.loginName)) {
                return false;
            }
            return true;
        }
    }
}
