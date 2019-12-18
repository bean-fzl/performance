package com.performance.oa.web.project;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.InitBinder;

import com.performance.oa.bo.EfficiencyProjectBo;
import com.performance.oa.entity.Efficiency;
import com.performance.oa.entity.Project;
import com.performance.oa.query.ProjectQuery;
import com.performance.oa.service.efficiency.EfficiencyService;
import com.performance.oa.service.project.ProjectService;

@Controller
@RequestMapping(value = "/admin/project")
public class ProjectAdminController {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private EfficiencyService efficiencyService;
    
    @RequestMapping(value = "/list")
    public String list(Model model) {
        return "projectAdmin/list";
    }
    
    @RequestMapping(value = "/listAjax")
    @ResponseBody
    public Page<EfficiencyProjectBo> listAjax(ProjectQuery p,Model model) {
        Page<EfficiencyProjectBo> page = projectService.findAllPro(p);
        return page;
    }
    
    @RequestMapping(value = "/cheack")
    public String cheack(@RequestParam(value = "proId") Long proId,Model model) {
        EfficiencyProjectBo efficiencyProject = projectService.findEfficiencyProjectByProId(proId);
        model.addAttribute("efficiencyProject", efficiencyProject);
        return "projectAdmin/cheackPage";
    }
    
    
  /*  @RequestMapping(value = "/projectList", method = RequestMethod.GET)
    public String projectList(ProjectQuery pQuery, Model model) {
        List<Project> list = projectService.find(pQuery);
        model.addAttribute("list", list);
        model.addAttribute("searchParames", pQuery);
        
        String beginTime = null;
        String endTim = null;
        
        //页面时间搜索回显
        if (null != pQuery.getProjectBeginTime()) {
        	beginTime = new SimpleDateFormat("yyyy-MM-dd").format(pQuery.getProjectBeginTime());
        }
        if (null != pQuery.getProjectEndTime()) {
        	endTim = new SimpleDateFormat("yyyy-MM-dd").format(pQuery.getProjectEndTime());
        }        
        
        model.addAttribute("projectBeginTime", beginTime);
        model.addAttribute("projectEndTime", endTim);
        model.addAttribute("state", pQuery.getState());
        return "project/projectList";
    }*/
    
    @RequestMapping(value = "/projectList", method = RequestMethod.GET)
    public String projectList(ProjectQuery pQuery, Model model) {
        return "project/projectList";
    }
   
    
    @RequestMapping(value = "/projectListQuery", method = RequestMethod.POST)
    @ResponseBody
    public Page<Project> projectListQuery(ProjectQuery pQuery, Model model) {
    	Page<Project> page = projectService.findQuery(pQuery);
    	model.addAttribute("page", page);
        model.addAttribute("searchParames", pQuery);
        
        String beginTime = null;
        String endTim = null;
        
        //页面时间搜索回显
        if (null != pQuery.getProjectBeginTime()) {
        	beginTime = new SimpleDateFormat("yyyy-MM-dd").format(pQuery.getProjectBeginTime());
        }
        if (null != pQuery.getProjectEndTime()) {
        	endTim = new SimpleDateFormat("yyyy-MM-dd").format(pQuery.getProjectEndTime());
        }        
        
        model.addAttribute("projectBeginTime", beginTime);
        model.addAttribute("projectEndTime", endTim);
        model.addAttribute("state", pQuery.getState());
        return page;
    }
   
    
    
    
    
    @RequestMapping(value = "/viewPage/{id}")
    public String projectView(@PathVariable int id, Model model) {
    	
    	List<Efficiency> effs=efficiencyService.findByProjectId(id);
    	model.addAttribute("effs",effs);
    	
    	Project project = projectService.findById(id);
    	model.addAttribute("project", project);
        return "project/projectView";
    }
    
    @InitBinder
    public void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
}
