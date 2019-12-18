package com.ync365.oa.web.leaderAssessment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ync365.commons.utils.CurrentUser;
import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.LeaderAssessment;
import com.ync365.oa.entity.PeController;
import com.ync365.oa.service.account.ShiroDbRealm.ShiroUser;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.leaderAssessment.LeaderAssessmentService;
import com.ync365.oa.service.pecontroller.PeControllerService;
import com.ync365.oa.service.properties.PropertiesService;

@Controller
@RequestMapping(value = "/leaderAssessment")
public class LeaderAssessmentController {

    @Autowired
    private LeaderAssessmentService assessmentService;
    @Autowired
    private EmployeService employeService;
    @Autowired
    private PeControllerService peControllerService;

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

    // 编辑页面
    @RequestMapping(value = "/editPage/{id}/{date}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") int id, @PathVariable("date")String date, Model model) {
    	Integer score = PropertiesService.LEADER_ASSESSMENT_SCORE;
    	LeaderAssessment leaderAssessment = assessmentService.selectById(id);
        model.addAttribute("assessment", leaderAssessment);
        model.addAttribute("score", score);
        model.addAttribute("date", date);
        model.addAttribute("serverDate", new Date());
        return "leaderAssessment/assessmentEdit";
    }

    // 部门MT责任人列表
    @RequestMapping(value = "/depList", method = RequestMethod.GET)
    public String mtList(@RequestParam(value="date",required=false) String date, Model model) {
        Map<String, Object> searchParams = new HashMap<String, Object>();
        Employe em = employeService.findOne(CurrentUser.getCurrentUser().id);
        Long departmentId = em.getDepartmentId();
        Page<PeController> pe = peControllerService.findByDepartmentId(departmentId);
        searchParams.put("departmentId", em.getDepartmentId());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Date d = Calendar.getInstance().getTime();
        if (StringUtils.isNotEmpty(date)) {
            try {
                d = f.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        searchParams.put("createTime", d);
        List<LeaderAssessment> assessments = assessmentService.getList(searchParams);
        List<Employe> employees = employeService.findAll();
        for (int i = 0; i < assessments.size(); i++) {
			for (int j = 0; j < employees.size(); j++) {
				Employe emp = employees.get(j);
				if (emp.getIsMt() && (emp.getName().equals(assessments.get(i).getBeEvaluatedName()))) {
					assessments.remove(i);
					continue;
				}
			}
		}
        
        model.addAttribute("assessments", assessments);
        model.addAttribute("pe", pe);
        model.addAttribute("createTime", d);
        return "leaderAssessment/assessmentDepList";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(LeaderAssessment assessment, ServletRequest request) {
        assessmentService.update(assessment);
        String date = request.getParameter("date");
        return "redirect:depList?date=" + date;
    }

    //员工查看页面
    @RequestMapping(value = "/employeeViewPage", method = RequestMethod.GET)
    public String employeeView(@Param("date") String date,Model model) {
        Long id = getCurrentUserId();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Date d = Calendar.getInstance().getTime();
        if (StringUtils.isNotEmpty(date)) {
            try {
                d = f.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        LeaderAssessment leaderAssessment = assessmentService.findByEmployeIdAndDate(id, d);
        model.addAttribute("assessment", leaderAssessment);
        Employe em = employeService.findOne(CurrentUser.getCurrentUser().id);
        Long departmentId = em.getDepartmentId();
        Page<PeController> pe = peControllerService.findByDepartmentId(departmentId);
        model.addAttribute("pe", pe);
        return "leaderAssessment/assessmentEmployeeView";
    }

    @InitBinder
    public void InitBinder(ServletRequestDataBinder bin) {
        bin.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }
}
