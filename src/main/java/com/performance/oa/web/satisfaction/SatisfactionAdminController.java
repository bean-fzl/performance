package com.ync365.oa.web.satisfaction;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.ync365.commons.utils.CloneUtils;
import com.ync365.oa.bo.SatisfactionBo;
import com.ync365.oa.bo.SatisfactionDetailPage;
import com.ync365.oa.entity.Satisfaction;
import com.ync365.oa.entity.SatisfactionResult;
import com.ync365.oa.query.SatisfactionQuery;
import com.ync365.oa.query.SatisfactionResultQuery;
import com.ync365.oa.service.satisfaction.SatisfactionService;
import com.ync365.oa.service.satisfactionresult.SatisfactionResultService;

@Controller
@RequestMapping(value = "/admin/satisfaction")
public class SatisfactionAdminController {

	@Autowired
	private SatisfactionService satisfactionServic;

	@Autowired
	private SatisfactionResultService satisfactionResultService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String index() {
		return "satisfactionAdmin/satisfactionList";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public Page<Satisfaction> list(SatisfactionQuery q, Model model) {
		Page<Satisfaction> page = null;
		page = satisfactionServic.findAllByPage(q);
		for (Satisfaction satisfaction : page.getContent()) {
			satisfaction.setEvaluatedName(
					satisfaction.getEvaluatedDepartmentName() + "," + satisfaction.getEvaluatedName());
			satisfaction.setBeEvaluatedName(
					satisfaction.getBeEvaluatedDepartmentName() + "," + satisfaction.getBeEvaluatedName());

		}
		return page;
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(Model model, @PathVariable Long id) {
		Satisfaction satisfaction = satisfactionServic.findOne(id);
		SatisfactionBo satisfactionBo =  new SatisfactionBo();
		CloneUtils.cloneObject(satisfaction, satisfactionBo);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		if(null != satisfaction.getEvaluatedTime()){
			satisfactionBo.setEvaluatedTimeFormate(f.format(satisfaction.getEvaluatedTime()));
		}
		model.addAttribute("satisfaction", satisfactionBo);
		return "satisfactionAdmin/satisfactionView";
	}

	@RequestMapping(value = "/resultList", method = RequestMethod.GET)
	public String listResult(Model model, SatisfactionResultQuery q) {
		Page<SatisfactionResult> page = null;
		page = satisfactionResultService.buildPageRequest(q);
		model.addAttribute("page", page);
		return "satisfactionAdmin/satisfactionResultList";
	}

	@RequestMapping(value = "/searchResult", method = RequestMethod.POST)
	@ResponseBody
	public Page<SatisfactionResult> searchResult(Model model, SatisfactionResultQuery q) {
		Page<SatisfactionResult> page = null;
		page = satisfactionResultService.buildPageRequest(q);
		return page;
	}

	@RequestMapping(value = "/viewResult/{id}", method = RequestMethod.GET)
	public String viewResult(Model model, @PathVariable Long id) {
		SatisfactionDetailPage satisfactionDetailPage = satisfactionServic.searchGroupByProject(id);
		model.addAttribute("satisfactionDetailPage", satisfactionDetailPage);
		return "satisfactionAdmin/satisfactionResultView";
	}

	/**
	 * 时间自动格式化
	 * 
	 * @author xieang 2015年9月15日
	 * @param bin
	 */
	@InitBinder
	public void InitBinder(ServletRequestDataBinder bin) {
		bin.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		bin.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		bin.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM"), true));
	}

}
