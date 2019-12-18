package com.ync365.oa.web.specialty;



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
import com.ync365.oa.bo.SpecialtyJXBo;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.PeController;
import com.ync365.oa.entity.SpecialtyHistory;
import com.ync365.oa.entity.SpecialtyJX;
import com.ync365.oa.service.account.ShiroDbRealm.ShiroUser;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.pecontroller.PeControllerService;
import com.ync365.oa.service.specialty.SpecialtyHistoryService;
import com.ync365.oa.service.specialty.SpecialtyJXService;

/**
 * 专业性绩效----前台控制层
 * 
 * @author lyh
 *
 */
@Controller
@RequestMapping(value = "/specialtyJX")
public class SpecialtyJXController {
    @Autowired
    private SpecialtyJXService specialtyJXService;
    @Autowired
    private EmployeService employeService;
    @Autowired
    private PeControllerService peControllerService;
    @Autowired
    private SpecialtyHistoryService specialtyHistoryService;

    /**
     * 某个员工的专业性绩效
     * 
     * @param beEvaluatedId
     * @param createTime
     * @param model
     * @return
     */
    @RequestMapping(value = "/employeJX")
    public String employeJX(@RequestParam(value = "date", required = false) Date createTime, Model model) {
        // 获取登录人的id
        ShiroUser currentUser = CurrentUser.getCurrentUser();
        Employe em = employeService.findOne(currentUser.id);
        if (createTime == null) {
            createTime = Calendar.getInstance().getTime();
        }
        SpecialtyJXBo employeJX = specialtyJXService.getSpecialtyJXByEmployeId(currentUser.id, createTime);
        model.addAttribute("employeJX", employeJX);
        Page<PeController> pe = peControllerService.findByDepartmentId(em.getDepartmentId());
        model.addAttribute("pe", pe);
        Calendar c = Calendar.getInstance();
        c.setTime(createTime);
        c.add(Calendar.MONTH, -1);
        model.addAttribute("date", c.getTime());
        return "specialtyJX/employeJX";
    }

    /**
     * 按月份获取某个MT负责人下的所有员工的专业性评价列表(本月评价上个月的)
     * 
     * @param beEvaluatedId
     * @param createTime
     * @param model
     * @return
     */
    @RequestMapping(value = "/employeJXList")
    public String list(@RequestParam(value = "date", required = false) Date createTime, Model model) {
        // 获取登录人的id
        ShiroUser currentUser = CurrentUser.getCurrentUser();
        if (createTime == null) {
            createTime = Calendar.getInstance().getTime();
        }
        Employe em = employeService.findOne(currentUser.id);
        List<SpecialtyJXBo> employeJXList  = new ArrayList<SpecialtyJXBo>();
        if(em!=null){
        	employeJXList = specialtyJXService.getAllSpecialtyJXByEvaluatedId(em.getDepartmentId(),
                    createTime);
        }
        
        List<SpecialtyHistory> speList = specialtyHistoryService.getSpecialtyListByDepartmentId(em.getDepartmentId(),createTime);
        model.addAttribute("employeJXList", employeJXList);
        model.addAttribute("speList", speList);
        Page<PeController> pe = peControllerService.findByDepartmentId(em.getDepartmentId());
        model.addAttribute("pe", pe);
        model.addAttribute("em", em);
        Calendar c = Calendar.getInstance();
        c.setTime(createTime);
        c.add(Calendar.MONTH, -1);
        model.addAttribute("date", c.getTime());
        return "specialtyJX/employeJXList";
    }

    /**
     * 去评价或修改专业性页面
     * 
     * @param beEvaluatedId
     * @param createTime
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/evaluatePage")   
    public String evaluate(Long beEvaluatedId, Date createTime, Model model){    	
		SpecialtyJXBo employeJX = specialtyJXService.getSpecialtyJXByEmployeId(beEvaluatedId, createTime);
        if(employeJX.getEvaluateTime()!=null){
        	model.addAttribute("operation", 2);//修改操作
        }else{
        	model.addAttribute("operation", 1);//评价操作
        }          
        model.addAttribute("employeJX", employeJX);
        return "specialtyJX/evaluate";   	
    }
    
    /**
     * 去评价页面之前先判断是否允许评分
     * 
     */
    @RequestMapping(value = "/isAllowedEvaluate")
    @ResponseBody
    public String isAllowedEvaluate() {
    	Boolean allowedEvaluate = specialtyJXService.isAllowedEvaluate();
    	String result = "";
    	if(allowedEvaluate){    		
            result = "OK";          
    	}else{
    		result = "NO";   		
    	}    
    	return result;
    }    
    
    /**
     * 专业性--绩效评价或修改绩效评价(每月1号至3号可以修改或评价上个月的专业性)
     * operation:----1表示评价,2表示修改
     */
    @RequestMapping(value = "/evaluateSpec", method = RequestMethod.POST)
    @ResponseBody
    public String evaluate(SpecialtyJXBo specialtyJXBo, int operation) {
        String result = "";
        try {
            // 获取登录人的id
            ShiroUser currentUser = CurrentUser.getCurrentUser();
            Employe mt = employeService.findOne(currentUser.id);
            if (mt!=null&&mt.getIsMt()) {//是否为部门负责人
                for (SpecialtyJX jx : specialtyJXBo.getSpecialtyJXList()) {
                    jx.setEvaluateId(mt.getId());
                    jx.setEvaluateName(mt.getName());
                    jx.setComment(specialtyJXBo.getComment());
                }
            }else{
            	result = "isNotMt";
            }
            Boolean flag = specialtyJXService.evaluate(specialtyJXBo.getSpecialtyJXList(), operation);
            if (flag) {
                result = "OK";
            }else {
                result = "Failed";
            }
        } catch (Exception e) {
            result = "Failed";
            e.printStackTrace();
        }
        return result;
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
