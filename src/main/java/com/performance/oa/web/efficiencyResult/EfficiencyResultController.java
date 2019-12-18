package com.ync365.oa.web.efficiencyResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.EfficiencyResult;
import com.ync365.oa.entity.Project;
import com.ync365.oa.query.EfficiencyQuery;
import com.ync365.oa.query.EfficiencyResultQuery;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.efficiencyResult.EfficiencyResultService;
import com.ync365.oa.service.project.ProjectService;

@Controller
@RequestMapping(value="/admin/efficiencyResult")
public class EfficiencyResultController {
	
	@Autowired
	private EfficiencyResultService  efficiencyResultService;
	
	@Autowired
	private DepartmentService  departmentService;
	
	@Autowired
	private ProjectService projectService;
	
	
	private List<Efficiency> eList=new ArrayList<Efficiency>();
	
	/***
	 * 页面列表
	 */
	@RequestMapping(value="/list",method =RequestMethod.GET)
	public String list(EfficiencyResultQuery erq,Model model){
		List<Department> d=departmentService.getAll();
		model.addAttribute("dept",d);
		return "efficiencyResult/efficiencyResultList";
	}
	
	@RequestMapping(value="efficiencyResultQuery",method =RequestMethod.POST)
	@ResponseBody
	public Page<EfficiencyResult> efficiencyResultQuery(EfficiencyResultQuery erQuery,Model model){
		List<Department> d=departmentService.getAll();
		model.addAttribute("dept",d);
		
		Page<EfficiencyResult> page =efficiencyResultService.find(erQuery);
		model.addAttribute("page", page);
		model.addAttribute("searchParams", erQuery);
		return page;
	}
	
	@RequestMapping(value="selectEfficiencyBypId/{pid}",method = RequestMethod.GET)
	@ResponseBody
	public List<Efficiency> selectEfficiencyBypId(@PathVariable Integer pid){
		List<Efficiency> ep=new ArrayList<Efficiency>();
			for(Efficiency e:eList){
				if(e.getProjectId() == pid){
					ep.add(e);
				}
			}	
			return ep;
	}
	/***
	 * 查看员工本月参与的项目
	 */
	
	@RequestMapping(value="/view/{id}",method = RequestMethod.GET)
	public String selectById(@PathVariable long id,Model model){
		//通过id查找出效能结果表
		EfficiencyResult er=efficiencyResultService.findOne(id);
		model.addAttribute("er", er);
		Calendar c=Calendar.getInstance();
		c.setTime(er.getCreateTime());
		c.add(Calendar.MONTH, -1);
		model.addAttribute("date", c.getTime());

		EfficiencyQuery eq=new EfficiencyQuery();
		/*查找某个月该名员工参加的项目*/
		eq.setEmployeId(er.getEmployeId());
		eq.setCreateTime(c.getTime()); 
		
		List<Efficiency> es=efficiencyResultService.findEmployeEfficiency(eq);
		model.addAttribute("es", es);
		List<Project> pros=new ArrayList<Project>();
		
		List<Integer> pn=new ArrayList<Integer>();
		Long minlong=Long.MAX_VALUE;
		Long maxlong =Long.MIN_VALUE;
		for(Efficiency e:es){
			if(e.getPlanBeginTime().getTime()<minlong){
				minlong=e.getPlanBeginTime().getTime();
			}
			if(e.getPlanEndTime().getTime()>maxlong){
				maxlong=e.getPlanEndTime().getTime();
			}
			if(!pn.contains(e.getProjectId())){
				pros.add(projectService.findById(e.getProjectId()));
				pn.add(e.getProjectId());
			}
		}
		HashSet<Integer> pih=new HashSet<Integer>(pn);
		model.addAttribute("pih", pih);
		model.addAttribute("pros", pros);
		model.addAttribute("beginTime", new Date(minlong));
		model.addAttribute("endTime", new Date(maxlong));
		
		eList=es;
		
		return "efficiencyResult/efficiencyResultForm";
	}
	
	@RequestMapping(value="selectBypId/{pid}",method = RequestMethod.GET)
	@ResponseBody
	public String selectBypId(@PathVariable Integer pid,Model model){
		Project p=projectService.findById(pid);
		if(p.getState()!=null){
				
		if(p.getState() == 1){
			return "已完成";
		}else{
			return "进行中";
		}
		
		}else{
			return "";	
		}
	}
	
	@InitBinder
    public void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM"), true));
    }
	
}
