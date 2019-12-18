package com.ync365.oa.web.employe;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ync365.commons.utils.CurrentUser;
import com.ync365.commons.utils.DepartmentConstant;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.query.EmployeQuery;
import com.ync365.oa.service.account.ShiroDbRealm.ShiroUser;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.employe.EmployeService;

@Controller
@RequestMapping(value="/admin/employe")
public class EmployeController {

	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private DepartmentService departmentService;
	
	/***
	 * 页面列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method =RequestMethod.GET)
	public String list(EmployeQuery eQuery,Model model){
		List<Department> d=departmentService.getAll();
		model.addAttribute("dept",d);
		model.addAttribute("searchParames",eQuery);
		return "employe/employeList";
	}
	
	@RequestMapping(value="employeQuery",method = RequestMethod.POST)
	@ResponseBody
	public Page<Employe> employeQuery(EmployeQuery eQuery,Model model){
		Page<Employe> page =employeService.find(eQuery);
		model.addAttribute("page",page);
		model.addAttribute("searchParams",eQuery);
		return page;
	}
	/***
	 * 导入员工的页面
	 * @return
	 */
	@RequestMapping(value="/addPage",method = RequestMethod.GET)
	public String addPage(Model model){
			List<Department> d=departmentService.getAll();
			model.addAttribute("dept",d);
			return "employe/addPage";
		}
	/***
	 * 导入员工
	 * @return
	 */
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public String add(Employe e){
		String passwd=e.getEmail();
		String loginName=passwd.substring(0,passwd.indexOf('@'));
		passwd=loginName+"123";
		e.setIsDel(0);
		e.setLoginName(loginName);
		e.setPassword(passwd);
		e.setCreateTime(Calendar.getInstance().getTime());
		employeService.add(e);
		
		/*查找所有的没有被删除的员工*/
		Long employeId=e.getId();
		Boolean isMt=e.getIsMt();
		String employeName=e.getName();
		Long deptId =e.getDepartmentId();
		Long mark=-1l;
		if (isMt) {
			if (deptId != null) {
				Department dept = departmentService.findOne(deptId);
				if (dept != null) {
					List<Employe> list = dept.getEs();
					Boolean flag = false;
					for (Employe employe : list) {
						if (employe.getId() != employeId ) {
							employe.setIsMt(flag);
							employeService.update(employe);
						}
					}
					dept.setMt(employeName);
					departmentService.update(dept);

				}

			}
		}
		return "redirect:/admin/employe/list";
	}
	/***
	 * 编辑页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/editPage/{id}",method = RequestMethod.GET)
	public String editPage(@PathVariable int id,Model model){
		Employe e=employeService.findOne(id);
		model.addAttribute("employe",e);
		
		List<Department> d=departmentService.getAll();
		model.addAttribute("dept",d);
		
		return "employe/editPage";
	}
	
	@RequestMapping(value="/edit/{id}",method = RequestMethod.POST)
	public String edit(@PathVariable int id ,Employe e){
		Employe employe=employeService.findOne(id);
		employe.setName(e.getName());
		employe.setCode(e.getCode());
		employe.setEmail(e.getEmail());
		employe.setLoginName(e.getEmail().substring(0,(e.getEmail()).indexOf('@')));
		employe.setMobile(e.getMobile());
		employe.setDepartmentId(e.getDepartmentId());
		employe.setDepartmentName(e.getDepartmentName());
		employe.setIsMt(e.getIsMt());
		employe.setIsPm(e.getIsPm());
		employe.setCreateTime(Calendar.getInstance().getTime());
		employe.setEmploymentDate(e.getEmploymentDate());
		int i=DepartmentConstant.DeptType.OTHER.v();
		employeService.update(employe);
		
		/*查找所有的没有被删除的员工*/
		Long employeId=employe.getId();
		Boolean isMt=employe.getIsMt();
		Long deptId =employe.getDepartmentId();
		String employeName=employe.getName();
		
		if (isMt == true) {
			if (deptId != null) {
				Department dept = departmentService.findOne(deptId);
				if (dept != null) {
					List<Employe> list = dept.getEs();
					Boolean flag = false;
					for (Employe e1 : list) {
						if (e1.getId() != employeId) {
							e1.setIsMt(flag);
							employeService.update(employe);
						}
					}
					dept.setMt(employeName);
					departmentService.update(dept);
				}
			}
		}
		return "redirect:/admin/employe/list";
	}
	/****
	 * 查看页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/view/{id}",method = RequestMethod.GET)
	public String selectById(@PathVariable int id,Model model){
		Employe e=employeService.findOne(id);
		model.addAttribute("employe",e);

		return "employe/employeForm";
	}
	/***
	 * 软删除员工
	 */
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public String delete(@PathVariable int id){
		Employe e=employeService.findOne(id);
		e.setIsDel(1);
		employeService.update(e);
		return "redirect:/admin/employe/list";
	}
	/***
	 * 密码重置
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/passwdReset/{id}",method = RequestMethod.GET)
	public String passwdReset(@PathVariable int id,Model model){
		Employe e=employeService.findOne(id);
		if(e!=null){
			String passwd=e.getEmail();
			passwd=passwd.substring(0,passwd.indexOf('@'))+"123";
			e.setPassword(passwd);
			employeService.update(e);
		}
		return "redirect:/admin/employe/list";
	}
	
	@RequestMapping(value="/checkLoginName/{loginName}",method = RequestMethod.GET)
	@ResponseBody
	public String checkName(@PathVariable String loginName){
		loginName=loginName.substring(0,loginName.indexOf('@'));
		
		EmployeQuery eq=new EmployeQuery();
		Page<Employe> lists=employeService.find(eq);
		String flag="YES";
		for(Employe e:lists){
			if(e.getLoginName().equals(loginName)){
				flag="NO";
				break;
			}
		}
		return flag;
	}
	
	@RequestMapping(value="/listAjax",method = RequestMethod.GET)
	@ResponseBody
    public List<Employe> listAjax(@RequestParam(value = "departmentId") Long departmentId){
        List<Employe> d=employeService.findByEmployeDepartmentId(departmentId);
        return d;
    }
	
	@InitBinder
    public void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true));
    }
	
}