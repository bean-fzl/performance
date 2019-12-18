package com.ync365.oa.web.properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ync365.oa.bo.PropertiesBo;
import com.ync365.oa.entity.Properties;
import com.ync365.oa.service.properties.PropertiesService;

@Controller
@RequestMapping("/admin/properties")
public class PropertiesController {
    @Autowired
    private PropertiesService propertiesService;

    @RequestMapping(method = RequestMethod.GET)
    public String updateForm(Model model,HttpServletRequest request) {
        Properties efficiency = propertiesService.findByName("efficiency_score");
        Properties leader_assessment = propertiesService.findByName("leader_assessment_score");
        Properties satisfaction = propertiesService.findByName("satisfaction_score");
        Properties specialty = propertiesService.findByName("specialty_score");
        model.addAttribute("efficiency", efficiency);
        model.addAttribute("leader_assessment", leader_assessment);
        model.addAttribute("satisfaction", satisfaction);
        model.addAttribute("specialty", specialty);
        model.addAttribute("info", request.getParameter("info"));
        return "properties/update";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(PropertiesBo bo, Model model) {
        propertiesService.update(bo);
        return "redirect:properties?info=success";
    }
}
