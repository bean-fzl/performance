package com.ync365.oa.web.leaderAssessment;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.LeaderAssessment;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.leaderAssessment.LeaderAssessmentService;
import com.ync365.oa.service.properties.PropertiesService;

@Controller
@RequestMapping(value = "/admin/leaderAssessment")
public class LeaderAssessmentAdminController {
	private static final String PAGE_SIZE = "10";
	@Autowired
	private LeaderAssessmentService assessmentService;
	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping(value = "/viewPage/{id}", method = RequestMethod.GET)
	public String seleteById(@PathVariable int id, Model model) {
		Integer score = PropertiesService.LEADER_ASSESSMENT_SCORE;
		LeaderAssessment leaderAssessment = assessmentService.selectById(id);
		model.addAttribute("assessment", leaderAssessment);
		model.addAttribute("score", score);
		return "leaderAssessment/assessmentView";
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Page<LeaderAssessment> list(@RequestParam(value = "pageIndex", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		pageNumber = pageNumber + 1;
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String score = (String) searchParams.get("score");
		Page<LeaderAssessment> assessments = assessmentService.getPage(searchParams, pageNumber, pageSize, sortType);
	
		model.addAttribute("assessments", assessments);
		model.addAttribute("sortType", sortType);
		model.addAttribute("searchParams", searchParams);
		model.addAttribute("score", score);
		
		return assessments;
	}
	
	@RequestMapping(value = "/listPage", method = RequestMethod.GET) 
	public String listPage (Model model) {
		List<Department> departments = departmentService.getAll();
		model.addAttribute("departments", departments);
		return "leaderAssessment/assessmentList";
	}
}
