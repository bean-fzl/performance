package com.ync365.oa.web.efficiency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ync365.commons.utils.CurrentUser;
import com.ync365.oa.bo.EfficiencyBo;
import com.ync365.oa.bo.EfficiencyViewVo;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.Project;
import com.ync365.oa.query.EfficiencyQuery;
import com.ync365.oa.service.account.ShiroDbRealm.ShiroUser;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.efficiency.EfficiencyService;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.project.ProjectService;




@Controller
@RequestMapping(value = "/efficiency")
public class EfficiencyController {
    
    @Autowired
    private EfficiencyService efficiencyService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private EmployeService employeService;
 

	@RequestMapping(value = "/list")
	public String list( @RequestParam(value = "proId") int proId,Model model) {
	    Project project = projectService.findById(proId);
	    model.addAttribute("project",project);
	    model.addAttribute("nDate", new Date());
		return "efficiency/list";
	}
	
	@RequestMapping(value = "/efficiencyListAjax" ,method = RequestMethod.POST)
    @ResponseBody
	public Page<Efficiency> efficiencyListAjax(EfficiencyQuery q ,Model model){
	    ShiroUser user = CurrentUser.getCurrentUser();
	    //根据登录人员id查询登录员工信息
        Employe employe = employeService.findOne(user.id);
        if(null != employe && null != employe.getIsPm() && null != employe.getIsMt()){
            if(!employe.getIsMt() && !employe.getIsPm()){
                q.setEmployeId(user.id.intValue());
            }
        }
	    Page<Efficiency> page = efficiencyService.findEfficiencyAll(q);
        return page;
	}
	
	@RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage() {
        return "efficiency/addPage";
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add(EfficiencyBo efficiencyBo ,Model model) {
	    ShiroUser user = CurrentUser.getCurrentUser();
	    Project project =  efficiencyService.add(efficiencyBo,user);
	    model.addAttribute("project",project);
        return "efficiency/listReload";
    }
	
	@RequestMapping(value = "/isfalse",method = RequestMethod.POST)
    public String isfalse(Model model) {
        return "efficiency/addPage";
    }
	
	@RequestMapping(value = "/editPage",method = RequestMethod.GET)
    public String editPage( @RequestParam(value = "proId") int proId,Model model) {
	    Project project = projectService.findById(proId);
	    List<Department> departmentList = departmentService.getAll();
	    List<EfficiencyViewVo> list = new ArrayList<EfficiencyViewVo>();
	    
	    list = efficiencyService.findListByProjectId(proId);
	    model.addAttribute("project",project);
	    model.addAttribute("list", list);
	    model.addAttribute("departmentList", departmentList);
	    model.addAttribute("nDate", Calendar.getInstance().getTime());
        return "efficiency/editPage";
    }
	
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String edit(EfficiencyBo efficiencyBo) {
	    ShiroUser user = CurrentUser.getCurrentUser();
	    efficiencyService.edit(efficiencyBo,user);
        return "efficiency/addPage";
    }

	@RequestMapping(value="/listAjax",method = RequestMethod.GET)
    @ResponseBody
    public Efficiency listAjax(@RequestParam(value = "hours") int hours,@RequestParam(value = "id") Long id,@RequestParam(value = "i") int i){
	    Efficiency efficiency = efficiencyService.addAhours(hours,id,i);
	    
	    return efficiency;
	}
	
	
	 /**时间自动格式化
     * @author xieang
     * 2015年9月15日
     * @param bin
     */
    @InitBinder
    public void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
