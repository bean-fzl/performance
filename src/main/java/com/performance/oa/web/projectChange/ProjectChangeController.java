package com.performance.oa.web.projectChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.performance.commons.utils.CurrentUser;
import com.performance.oa.entity.Employe;
import com.performance.oa.entity.ProjectChange;
import com.performance.oa.query.ProjectChangeQuery;
import com.performance.oa.service.account.ShiroDbRealm.ShiroUser;
import com.performance.oa.service.employe.EmployeService;
import com.performance.oa.service.projectChange.ProjectChangeService;




@Controller
@RequestMapping(value = "/projectchange")
public class ProjectChangeController {
    
    @Autowired
    private ProjectChangeService projectChangeService;
    
    @Autowired
    private EmployeService employeService;

    @RequestMapping(value = "/listAjax" ,method = RequestMethod.POST)
    @ResponseBody
    public Page<ProjectChange> efficiencyListAjax( ProjectChangeQuery q ,Model model){
        ShiroUser user = CurrentUser.getCurrentUser();
      //根据登录人员id查询登录员工信息
        Employe employe = employeService.findOne(user.id);
        if(null != employe && null != employe.getIsPm() && null != employe.getIsMt()){
            if(!employe.getIsMt() && !employe.getIsPm()){
                q.setEmployeId(user.id.intValue());
            }
        }
        Page<ProjectChange> page = projectChangeService.findAll(q);
        return page;
    }
    
}
