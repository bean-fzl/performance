package com.ync365.oa.web.performanceevaluation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ync365.commons.utils.CurrentUser;
import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.bo.PerformanceEvaluationBo;
import com.ync365.oa.bo.SpecialtyJXBo;
import com.ync365.oa.entity.EfficiencyResult;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.PeController;
import com.ync365.oa.entity.PerformanceEvaluation;
import com.ync365.oa.query.PerformanceEvaluationQuery;
import com.ync365.oa.service.efficiencyResult.EfficiencyResultService;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.pecontroller.PeControllerService;
import com.ync365.oa.service.performanceevaluation.PerformanceEvaluationService;
import com.ync365.oa.service.specialty.SpecialtyJXService;

@Controller
@RequestMapping(value = "/performanceEvaluation")
public class PerformanceEvaluationController {
    @Autowired
    private PerformanceEvaluationService performanceEvaluationService;
    @Autowired
    private PeControllerService peControllerService;
    @Autowired
    private EmployeService employeService;
    @Autowired
    private EfficiencyResultService efficiencyResultService;
    @Autowired
    private SpecialtyJXService specialtyJXService;

    @RequiresRoles("mt")
    @RequestMapping(value = "list")
    public String list(@Param("date") String date, Model model) {
        Employe em = employeService.findOne(CurrentUser.getCurrentUser().id);
        String departmentName = em.getDepartmentName();
        Long departmentId = em.getDepartmentId();
        PerformanceEvaluationQuery q = new PerformanceEvaluationQuery();
        q.setDepartmentName(departmentName);
        q.setSort("totalScore");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Date d = Calendar.getInstance().getTime();
        if (StringUtils.isNotEmpty(date)) {
            try {
                d = f.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        q.setCreateTime(d);
        Page<PerformanceEvaluation> page = performanceEvaluationService.find(q);
        double sum = 0;
        for (PerformanceEvaluation performanceEvaluation : page) {
            sum += performanceEvaluation.getTotalScore();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, -1);
        model.addAttribute("date", c.getTime());
        if(page.getTotalElements()>0){
        	int index=(int) Math.ceil(page.getTotalElements()/2.0);
        	
        	double avg = page.getContent().get(index-1).getTotalScore();
        	model.addAttribute("avg", avg);
        }
        Page<PeController> pe = peControllerService.findByDepartmentId(departmentId);
        model.addAttribute("pe", pe);
        model.addAttribute("page", page);
        model.addAttribute("em", em);
        model.addAttribute("dateMonth", q.getCreateTime().getMonth() + 1);
        return "performanceevaluation/performanceEvaluationListForm";
    }

    @RequiresRoles("mt")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(PerformanceEvaluationBo bo, Model model) {
        int s = 0;
        int d = 0;
        for (PerformanceEvaluation item : bo.getPerformanceEvaluation()) {
            if ("S".equals(item.getTotalGrade())) {
                s++;
            } else if ("D".equals(item.getTotalGrade())) {
                d++;
            }
        }
        List<String> listgrad = new ArrayList<>();
        listgrad.add("S");
        listgrad.add("A");
        if (s >= 2 && d < 1) {

        } else {
            for (PerformanceEvaluation item : bo.getPerformanceEvaluation()) {
                PerformanceEvaluation entity = performanceEvaluationService.findOne(item.getId());
                entity.setTotalGrade(item.getTotalGrade());
                performanceEvaluationService.update(entity);
            }
        }
        return "redirect:list";
    }

    @RequestMapping("detail")
    public String detail(@RequestParam(value = "date", required = false) String date, Model model) {
        Employe em = employeService.findOne(CurrentUser.getCurrentUser().id);
        String departmentName = em.getDepartmentName();
        Long departmentId = em.getDepartmentId();
        String name = em.getName();
        PerformanceEvaluationQuery q = new PerformanceEvaluationQuery();
        q.setDepartmentName(departmentName);
        q.setBeEvaluatedName(name);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Date d = Calendar.getInstance().getTime();
        if (StringUtils.isNotEmpty(date)) {
            try {
                d = f.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        q.setCreateTime(d);
        Page<PerformanceEvaluation> page = performanceEvaluationService.find(q);
        PerformanceEvaluation p = page.getTotalElements() > 0 ? page.getContent().get(0) : null;
        Page<PeController> pe = peControllerService.findByDepartmentId(departmentId);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, -1);
        model.addAttribute("date", c.getTime());
        model.addAttribute("pe", pe);
        model.addAttribute("p", p);
        EfficiencyResult efficiencyResult = efficiencyResultService.findEfficiencyResult(em.getId(), d);
       if(null!=efficiencyResult){
           String fuhelv = String.format("%s%%", efficiencyResult.getLoadRate());
           String xiaolv = String.format("%s%%", efficiencyResult.getEfficiencyPercentage());
           model.addAttribute("fuhelv", fuhelv);
           model.addAttribute("xiaolv", xiaolv);
       }
        SpecialtyJXBo specialtyJXBo = specialtyJXService.getSpecialtyJXByEmployeId(em.getId(), d);
        model.addAttribute("specialtyJXBo", specialtyJXBo);
        return "performanceevaluation/performanceEvaluationInfo";
    }
}
