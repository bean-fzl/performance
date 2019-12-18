package com.ync365.oa.web.satisfaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ync365.commons.utils.CloneUtils;
import com.ync365.commons.utils.CurrentUser;
import com.ync365.oa.bo.SatisfactionBo;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.Satisfaction;
import com.ync365.oa.repository.EfficiencyDao;
import com.ync365.oa.service.properties.PropertiesService;
import com.ync365.oa.service.satisfaction.SatisfactionService;

@Controller
@RequestMapping(value = "/satisfaction")
public class SatisfactionController {
	
	@Autowired
	private SatisfactionService satisfactionServic;
	
	@Autowired
	private PropertiesService propertiesService;
	
	@Autowired
	private EfficiencyDao efficiencyDao;
	
	@RequestMapping(value = "/main" , method = RequestMethod.GET)
	public String main(Model model) {
        return "satisfaction/mainMenu";
	}
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list(Model model,@Param Long id) {
		Long userId = CurrentUser.getCurrentUser().id;
		List <Satisfaction> myEvaluateTemp = new ArrayList<Satisfaction>() ;//我评价的临时
		List <SatisfactionBo> myEvaluateList = new ArrayList<SatisfactionBo>() ;//我评价的
		List<Satisfaction> myEvaluatedList = new ArrayList<Satisfaction>();//评价我的
		List<Satisfaction> projectList = new ArrayList<Satisfaction>();//项目列表
		Date curDate = new Date();//获取项目时间，用于判断评价和修改按钮是否显示
		projectList = satisfactionServic.searchProjectListByUserId(userId);
		if(id == null && projectList.size() > 0) {
			id = projectList.get(0).getProjectId();
		}
		myEvaluateTemp = satisfactionServic.searchByEvaIdProId(userId,id);
		for (Satisfaction satisfaction : myEvaluateTemp) {
			
			List<Efficiency> list = efficiencyDao.findByProjectIdAndEmployeId(satisfaction.getProjectId().intValue(), satisfaction.getBeEvaluatedId().intValue());
			Boolean flagtest = false;
			for(Efficiency efficiency : list) {
				if(efficiency.getPlanBeginTime().before(curDate)){
					flagtest = true;
				}
			}
			SatisfactionBo satisfactionBo = new SatisfactionBo();
			CloneUtils.cloneObject(satisfaction, satisfactionBo);
			satisfactionBo.setHavaButton(flagtest);
			myEvaluateList.add(satisfactionBo);
		}
		myEvaluatedList = satisfactionServic.searchByBeEvaIdProId(userId,id);
		model.addAttribute("myEvaluateList", myEvaluateList);
		model.addAttribute("myEvaluatedList", myEvaluatedList);
		model.addAttribute("projectList", projectList);
		model.addAttribute("flag",true);
        return "satisfaction/satisfactionList";
	}
	@RequestMapping(value = "/editPage/{id}", method = RequestMethod.GET )
	public String editPage(Model model,@PathVariable Long id) {
		Long userId = CurrentUser.getCurrentUser().id;
		Satisfaction satisfaction = satisfactionServic.findByIdEvaluatedId(id,userId);
		SatisfactionBo satisfactionBo = new SatisfactionBo();
		CloneUtils.cloneObject(satisfaction, satisfactionBo);
		satisfactionBo.setScoreLimit(Double.parseDouble(String.valueOf(propertiesService.SATISFACTION_SCORE)));
		model.addAttribute("satisfaction",satisfactionBo);
        return "satisfaction/satisfactionEdit";
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editPage(Model model,Satisfaction satisfaction) {
		Long userId = CurrentUser.getCurrentUser().id;
		Satisfaction temp = satisfactionServic.findOne(satisfaction.getId());
		if(temp.getEvaluatedId().equals(userId)) {
			satisfaction.setEvaluatedTime(new Date());
			satisfactionServic.update(satisfaction);
		}
        return "redirect:list?id="+satisfaction.getProjectId();
	}
	/**时间自动格式化
	    * @author xieang
	    * 2015年9月15日
	    * @param bin
	    */
	  @InitBinder
	   public void InitBinder(ServletRequestDataBinder bin) {
	       bin.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	   }
}
