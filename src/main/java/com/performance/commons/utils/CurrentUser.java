package com.performance.commons.utils;

import org.apache.shiro.SecurityUtils;

import com.performance.oa.service.account.ShiroDbRealm.ShiroUser;

public class CurrentUser {
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public static ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
}
