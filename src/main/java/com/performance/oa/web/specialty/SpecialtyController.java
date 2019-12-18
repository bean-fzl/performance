package com.ync365.oa.web.specialty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ync365.oa.bo.SpecialtyJXBo;
import com.ync365.oa.bo.SpecialtyScoreBo;
import com.ync365.oa.bo.SpecialtyVo;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Specialty;
import com.ync365.oa.entity.SpecialtyHistory;
import com.ync365.oa.query.SpecialtyQuery;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.specialty.SpecialtyHistoryService;
import com.ync365.oa.service.specialty.SpecialtyJXService;
import com.ync365.oa.service.specialty.SpecialtyService;

/**
 * 专业性----后台控制层
 * @author lyh
 *
 */
@Controller
@RequestMapping(value = "/admin/specialty")
public class SpecialtyController {
	@Autowired
	private SpecialtyService specialtyService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SpecialtyJXService specialtyJXService;
	@Autowired
	private SpecialtyHistoryService specHistoryService;
	
	/**
	 * 所有部门的专业性列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		List<Specialty> specialtyList = specialtyService.getAllSpecialty();
		List<Department> departmentList = departmentService.getAll();
		model.addAttribute("specialtyList", specialtyList);
		model.addAttribute("departmentList", departmentList);
		return "specialty/list";
	}
	
	/**
	 * 多条件组合查询某个部门的专业性
	 * @param specialty
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/query")
	public String list(SpecialtyQuery specialty,Model model) {
		List<Specialty> specialtyList = specialtyService.getSpecialtyByMultiple(specialty);
		List<Department> departmentList = departmentService.getAll();
		model.addAttribute("specialtyList", specialtyList);
		model.addAttribute("departmentList", departmentList);		
		model.addAttribute("departmentId", specialty.getDepartmentId());
		model.addAttribute("specialty", specialty);
		return "specialty/list";
	}	
	
	/**
	 * 弹窗----添加专业性
	 * @return
	 */
	@RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(Model model) {
		List<Department> departmentList = departmentService.getAll();		
		model.addAttribute("departmentList", departmentList);	
        return "specialty/add";
    }
	/**
	 * 异步获取部门负责人
	 * @param model
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value = "/getMt",method = RequestMethod.GET)
	@ResponseBody
	public Department getMt(Model model,Long departmentId) {		
		Department department = departmentService.findOne(departmentId);		
		model.addAttribute("department", department);		
		return department;
	}
	/**
	 * 添加专业性
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)	
	@ResponseBody
    public String add(SpecialtyVo specialtyVo) {
		int num = specialtyService.checkSpecialtyNumByDepartmentId(specialtyVo.getDepartmentId());
		String result = "";
		try {
			if(num==0){
				specialtyService.addSpecialty(specialtyVo);
				result = "OK";
			}else if(num==1){
				specialtyService.addSpecialty(specialtyVo);				
				result = "one";
			}else if(num==2){
				result = "two";
			}			
		}catch (Exception e) {	
			result = "Failed";
			e.printStackTrace();
		}
        return result;
    }
	
	/**
	 * 弹窗----删除专业性
	 * @return
	 */
	@RequestMapping(value = "/deletePage",method = RequestMethod.GET)
	public String deletePage(Long specialtyId,Model model) {
		Specialty specialty = specialtyService.getSpecialtyById(specialtyId);		
		model.addAttribute("specialty", specialty);
		return "specialty/delete";
	}	
	
	/**
	 * 删除专业性
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(Long specialtyId) {
		String result = "";
		try {
			Specialty specialty = specialtyService.getSpecialtyById(specialtyId);			
			Long departmentId = specialty.getDepartmentId();
			specialtyService.deleteSpecialty(specialtyId);			
			int num = specialtyService.checkSpecialtyNumByDepartmentId(departmentId);
			if(num==0){
				result = "no";
			}else if(num==1){
				result = "one";
			}			
		} catch (Exception e) {	
			result = "Failed";
			e.printStackTrace();
		}
        return result;		
	}
	/**
	 * 根据多条件组合查询员工的专业性得分列表
	 * @return
	 */	
	@RequestMapping(value = "/employeScore")
	public String getSpecialtyScoreList(SpecialtyScoreBo specialtyScoreBo,Model model){		
		List<Department> depList = departmentService.getAll();
		if(depList!=null&&depList.size()>0){			  		
			if(specialtyScoreBo.getDepartmentId() == null){
				specialtyScoreBo.setDepartmentId(depList.get(0).getId());					        	
		     	model.addAttribute("queryTime",null);		        
			}else{
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");				
				if(specialtyScoreBo.getCreateTime()!=null){
					String format = f.format(specialtyScoreBo.getCreateTime());
					model.addAttribute("queryTime",format);//回显查询时间
				}				
			}			
		}
		if(specialtyScoreBo.getCreateTime()==null){
			specialtyScoreBo.setCreateTime(new Date());
		}
		List<SpecialtyScoreBo> employeScoreList = specialtyJXService.getSpecialtyJXByMultiple(specialtyScoreBo);
		List<SpecialtyHistory>	speHistoryList = specHistoryService.getSpecialtyListByDepartmentId(specialtyScoreBo.getDepartmentId(),specialtyScoreBo.getCreateTime());									
		model.addAttribute("departmentList", depList);		
        model.addAttribute("employeScoreList", employeScoreList);
        model.addAttribute("speList", speHistoryList);
        model.addAttribute("departmentId", specialtyScoreBo.getDepartmentId());
        model.addAttribute("specialtyQuery",specialtyScoreBo);//作为回显数据使用
            
		return "specialty/specialtyScoreList";
	}
	
	/**
     * 查看某个员工的专业性得分详情
     * 
     * @param beEvaluatedId
     * @param createTime
     * @param model
     * @return
     */
    @RequestMapping(value = "/lookScoreDetail")
    public String lookScoreDetail(Date createTime, Long beEvaluatedId,Model model) {        
        if (createTime == null) {
        	createTime = Calendar.getInstance().getTime();
        }
        SpecialtyJXBo employeScoreDetail = specialtyJXService.getSpecialtyJXByEmployeId(beEvaluatedId, createTime);
        model.addAttribute("employeScoreDetail", employeScoreDetail);        
        return "specialty/scoreDetail";
    }   
    
    /**
     * 将string类型的时间自动转换成date类型
     * @author lyh
     * @param bin
     */
    @InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM"), true));
    }
	
}
