package com.ync365.oa.service.efficiency;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.bo.EfficiencyBo;
import com.ync365.oa.bo.EfficiencyRecordBo;
import com.ync365.oa.bo.EfficiencyViewVo;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.Project;
import com.ync365.oa.entity.ProjectChange;
import com.ync365.oa.query.EfficiencyQuery;
import com.ync365.oa.repository.DepartmentDao;
import com.ync365.oa.repository.EfficiencyDao;
import com.ync365.oa.repository.EmployeDao;
import com.ync365.oa.repository.ProjectChangeDao;
import com.ync365.oa.repository.ProjectDao;
import com.ync365.oa.service.account.ShiroDbRealm.ShiroUser;
import com.ync365.oa.service.satisfaction.SatisfactionService;


@Component
@Transactional
public class EfficiencyService {
    
    private static Logger logger = LoggerFactory.getLogger(EfficiencyService.class);
    
    @Autowired
    private EfficiencyDao efficiencyDao;
    
    @Autowired
    private ProjectDao projectDao;
    
    @Autowired
    private DepartmentDao departmentDao;
    
    @Autowired
    private EmployeDao employeDao;
    
    @Autowired
    private ProjectChangeDao projectChangeDao;
    
    @Autowired
    private SatisfactionService satisfactionService;
    
    /**
     * 添加   效能新建添加方法
     * @param efficiencyBo
     * @param user 
     * @return 
     */
    public Project add(EfficiencyBo efficiencyBo, ShiroUser user) {
        
        Project project_t = new Project();
        //添加 project
        Map<String,Object> mapT = this.addProject(efficiencyBo,project_t);
        Project project = (Project) mapT.get("project");
        //添加创建时间
        project.setCreateTime(Calendar.getInstance().getTime());
        //添加项目状态 0 进行中1 完成
        project.setState(0);
        //添加项目经理名称
        project.setPm(user.name);
        //添加项目经理相关id
        project.setPmId(user.id.intValue());
        //调用保存方法
        projectDao.save(project);
        
        //创建list用于封装数据 用于客户满意度使用
        List<Efficiency> efficiency_list_t = new ArrayList<Efficiency>();
        
        //添加到efficiency 
        efficiency_list_t = addEff(efficiencyBo,mapT,user ,efficiency_list_t);
        
        //客户满意度数据插入
        satisfactionService.insertSatisfactionByEfficiency(efficiency_list_t);
        
        return project;
    }
    

    /**
     * 更加projectid查询List<Efficiency>
     * @param proId
     * @return
     */
    public List<Efficiency> findByProjectId(int proId) {
        return efficiencyDao.findByProjectId(proId);
    }
    
    
    /**
     * 根据projectid查询List<EfficiencyViewVo>
     * 查询 部门对应的所有 员工list列表
     * @param proId
     * @return
     */
    public List<EfficiencyViewVo> findListByProjectId(int proId) {
        List<EfficiencyViewVo> efficiencyViewVoList = new ArrayList<EfficiencyViewVo>();
        
        List<Efficiency> efficiencylist = efficiencyDao.findByProjectId(proId);
        
        List<Employe> employeList = null;
        if(null != efficiencylist && efficiencylist.size() > 0 ){
            for(Efficiency temp : efficiencylist){
                EfficiencyViewVo efficiencyViewVo = new EfficiencyViewVo();
                BeanUtils.copyProperties(temp, efficiencyViewVo);//拷贝对象属性
                employeList = employeDao.findByDepartmentId(temp.getDepartmentId().longValue());
                efficiencyViewVo.setEmployeList(employeList);
                efficiencyViewVoList.add(efficiencyViewVo);
            }
        }
        return efficiencyViewVoList;
    }
    
    /**
     * 编辑 
     * @param efficiencyBo
     * @param user 
     */
    public void edit(EfficiencyBo efficiencyBo, ShiroUser user) {
        //根据project 主键id查询已经存在的project对象
        Project project_t  = projectDao.findOne(efficiencyBo.getProId());
        //添加 project
        Map<String,Object> mapT = this.addProject(efficiencyBo,project_t);
        Project project = (Project) mapT.get("project");
        //更新对象
        projectDao.save(project);
        
        //创建list用于封装数据 用于客户满意度使用
        List<Efficiency> efficiency_list_t = new ArrayList<Efficiency>();
        
        if(null != efficiencyBo.getEfficiencyRecordBo() && efficiencyBo.getEfficiencyRecordBo().size() > 0){
            //根据项目id去efficiency查询此项目id下所有的参与人员
            List<Efficiency> old_eff_list = this.findByProjectId(Integer.parseInt(String.valueOf(efficiencyBo.getProId())));
            //如果old_eff_list 为null  则else  新增保存
            if(null != old_eff_list && old_eff_list.size() > 0){
                //存放需要修改的集合
                List<Efficiency> udate_list= new ArrayList<Efficiency>();
                
                List<EfficiencyRecordBo> add_efficiency_list = new ArrayList<EfficiencyRecordBo>();
                
                //存放需要删除的集合
                List<Efficiency> del_list= new ArrayList<Efficiency>();
                
                /**
                 * 已查询出来的效能列表 old_eff_list
                 * 修改时前台新提交的 efficiencyBo.getEfficiencyRecordBo()
                 * 循环已存在的效能列表  通过效能id与新提交的列表比较 
                 * 存在相等的 则是需要修改的
                 * 
                 */
                for(Efficiency temp : old_eff_list){
                    if(null != temp && null != temp.getId()){
                        for(EfficiencyRecordBo temp_eff_one : efficiencyBo.getEfficiencyRecordBo()){
                            if(null != temp_eff_one){
                                if(null != temp_eff_one.getEfficiencyIds()){
                                    if(temp.getId().intValue() == temp_eff_one.getEfficiencyIds().intValue()){
                                        //需要修改的数据列表
                                        udate_list.add(temp);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                
                /**
                 * 通过已经查询出来需要修改的数据  查询
                 * 循环查询出来需要删除的数据
                 */
                for(Efficiency temp_h : old_eff_list){
                    if(null != temp_h){
                        if(!udate_list.contains(temp_h)){
                            //需要删除的对象数据 列表
                            del_list.add(temp_h);
                        }
                    }
                }
                
                /**
                 * 查找出需要新增的数据列表
                 * 主键id为空 其他不为空的
                 * 
                 */
                for(EfficiencyRecordBo temp_eff_two : efficiencyBo.getEfficiencyRecordBo()){
                    if(null == temp_eff_two.getEfficiencyIds() && (null != temp_eff_two.getDepartmentId() || 
                        null != temp_eff_two.getEmployeId() || null != temp_eff_two.getPlanHours() ||
                        null != temp_eff_two.getPlanBeginTime() || null != temp_eff_two.getPlanEndTime())){
                        add_efficiency_list.add(temp_eff_two);
                    }
                }
                
                List<Efficiency> temp_list_eff  = addEfficiencyByUadate(add_efficiency_list,user,project);
                if(null != temp_list_eff && temp_list_eff.size()>0){
                    for(Efficiency eff_:temp_list_eff){
                        efficiency_list_t.add(eff_);
                    }
                }
                
                
                //删除修改之后不要的数据
                if(null != del_list && del_list.size() > 0 ){
                    efficiencyDao.delete(del_list);
                    //删除时需要调用客户满意度方法
                    deleteSatisfactionByEffciencyList(del_list,old_eff_list);
                    //删除修改记录中的信息
                    delProjectChange(del_list,user);
                }
                
                //修改时  需要修改的 数据
                if(null != udate_list && udate_list.size() > 0){
                    List<Efficiency> efficiency_list_t_updat = updateEff(udate_list,efficiencyBo ,new ArrayList<Efficiency>(),user);
                    if(null != efficiency_list_t_updat && efficiency_list_t_updat.size() > 0){
                        for(Efficiency c : efficiency_list_t_updat){
                            efficiency_list_t.add(c);
                        }
                    }
                }
            }else{
                efficiency_list_t = addEff(efficiencyBo,mapT,user,new ArrayList<Efficiency>());
            }
        }
        
        //客户满意度数据插入
        satisfactionService.insertSatisfactionByEfficiency(efficiency_list_t);
    }
    

    /**
     * 删除时需要删除客户满意度信息中的相关记录
     * @param old_eff_list 
     * @param List<Efficiency> del_list
     */
    private void deleteSatisfactionByEffciencyList(List<Efficiency> del_list, List<Efficiency> old_eff_list) {
        if(null != del_list && del_list.size() > 0){
            List<Efficiency> efficiencySatisfactionList = new ArrayList<Efficiency>();
            int a = 0;
            if(null != old_eff_list && old_eff_list.size() > 0 ){
                for(Efficiency eff_one : del_list){
                    for(Efficiency eff_three : old_eff_list){
                        if(eff_three.getEmployeId().intValue() == eff_one.getEmployeId().intValue()){
                            a=a+1;
                        }
                    }
                    if(a<=1){
                        efficiencySatisfactionList.add(eff_one);
                        a=0;
                    }else{
                        a=0;
                    }
                }
            }else{
                for(Efficiency eff_two : del_list){
                    efficiencySatisfactionList.add(eff_two);
                }
            }
            //客户满意度方法
            satisfactionService.deleteSatisfactionByEffciency(efficiencySatisfactionList);
        }
    }


    /**
     * 修改时新增的效能 人员
     * @param List<EfficiencyRecordBo> add_efficiency_list
     * @param user
     * @param project 
     * @return
     */
    private List<Efficiency> addEfficiencyByUadate(List<EfficiencyRecordBo> add_efficiency_list, ShiroUser user, Project project) {
        List<Efficiency> efficiency_list_th = new ArrayList<Efficiency>();
        Employe ee_emp  = null;
        Department dett  = null;
        if(null != add_efficiency_list && add_efficiency_list.size() > 0){
            for(EfficiencyRecordBo temp_o : add_efficiency_list){
                Efficiency efficiency_record = new Efficiency();
              //添加创建时间  项目名称  及 及创建人名称
                efficiency_record.setCreateTime(Calendar.getInstance().getTime());
                if(null != project.getName()&& "" != project.getName()){
                    efficiency_record.setProjectName(project.getName());
                }
                //获取项目id —— ProjectId
                if(null != project && null != project.getId()){
                    efficiency_record.setProjectId(project.getId().intValue());
                }
                //添加项目状态
                if(null != project.getState()){
                    efficiency_record.setProjectState(project.getState());
                }
                if(null != user && null != user.getName() && "" != user.getName()){
                    efficiency_record.setPm(user.getName());
                }
                
                //添加员工信息
                if(null != temp_o.getEmployeId()){
                    ee_emp = employeDao.findOne(temp_o.getEmployeId().longValue());
                    if(null != ee_emp){
                        if(null != ee_emp.getId()){
                            efficiency_record.setEmployeId(ee_emp.getId().intValue());
                        }
                        if(null != ee_emp.getCode() && "" != ee_emp.getCode()){
                            efficiency_record.setEmployeCode(ee_emp.getCode()); 
                        }
                        if(null != ee_emp.getName() && "" != ee_emp.getName()){
                            efficiency_record.setEmployeName(ee_emp.getName());
                        }
                    }
                    
                }
                
                //判断部门id  根据部门id查询部门对象
                if(null != temp_o.getDepartmentId() ){
                    dett = departmentDao.findOne(temp_o.getDepartmentId().longValue());
                    if(null != dett){
                        if(null != dett.getName() && "" != dett.getName()){
                            efficiency_record.setDepartmentName(dett.getName()); 
                        }
                    }
                    efficiency_record.setDepartmentId(temp_o.getDepartmentId());
                }   
                //添加计划时间   
                if(null != temp_o.getPlanHours()) {
                    efficiency_record.setPlanHours(temp_o.getPlanHours());
                }
                
                //添加时间
                if(null != temp_o.getPlanBeginTime() ){
                    efficiency_record.setPlanBeginTime(temp_o.getPlanBeginTime());
                    efficiency_record.setActualBeginTime(temp_o.getPlanBeginTime());
                    efficiency_record.setOutputBeginTime(temp_o.getPlanBeginTime());
                }  
                    
                if(null != temp_o.getPlanEndTime()) {
                    efficiency_record.setPlanEndTime(temp_o.getPlanEndTime());
                    efficiency_record.setActualEndTime(temp_o.getPlanEndTime());
                    efficiency_record.setOutputEndTime(temp_o.getPlanEndTime());
                }  
                    
                //保存efficiency对象
                efficiencyDao.save(efficiency_record);
                //添加 project_change信息
                addFirstProjectChange(efficiency_record , user);
                //封装list数据
                efficiency_list_th.add(efficiency_record);
                
            }
        }
        return efficiency_list_th;
    }



    /**
     * 封装更新 efficiency
     * @param udate_list
     * @param efficiencyBo
     * @param efficiency_list_t 
     * @param user 
     * @return 
     */
    private List<Efficiency> updateEff(List<Efficiency> udate_list, EfficiencyBo efficiencyBo, List<Efficiency> efficiency_list_t, ShiroUser user) {
        if(null != udate_list &&  udate_list.size() > 0  && null != efficiencyBo  ){
            /*begin*/
            Department det_t = null;
            Employe  ee_t = null;
            Efficiency efficiency_old_temp = new Efficiency();//创建对象 为拷贝对象使用
            //此处为拿出列表
            /* begin*/
            List<EfficiencyRecordBo> list_t_t = null;
            if(null != efficiencyBo.getEfficiencyRecordBo()){
                list_t_t = efficiencyBo.getEfficiencyRecordBo() ;
            }
            /*end */
            for(Efficiency eff_t : udate_list){
                /*begin*/
                if(null !=  list_t_t && list_t_t.size() > 0){
                    for(EfficiencyRecordBo eff_re_bo : list_t_t){
                        if(null != eff_re_bo.getEfficiencyIds() &&  null != eff_t.getId() && 
                                eff_t.getId().intValue() == eff_re_bo.getEfficiencyIds().intValue()){
                            
                            BeanUtils.copyProperties(eff_t, efficiency_old_temp);//拷贝对象属性
                            
                            eff_t.setEmployeName(efficiencyBo.getProjectNameBo());
                            
                            eff_t.setPlanBeginTime(eff_re_bo.getPlanBeginTime());
                            eff_t.setActualBeginTime(eff_re_bo.getPlanBeginTime());
                            eff_t.setOutputBeginTime(eff_re_bo.getPlanBeginTime());
                            
                            eff_t.setPlanEndTime(eff_re_bo.getPlanEndTime());
                            eff_t.setActualEndTime(eff_re_bo.getPlanEndTime());
                            eff_t.setOutputEndTime(eff_re_bo.getPlanEndTime());
                            
                            eff_t.setPlanHours(eff_re_bo.getPlanHours());
                            
                            if(null != eff_re_bo.getDepartmentId()){
                                //根据id查询对象
                                det_t = departmentDao.findOne(eff_re_bo.getDepartmentId().longValue());
                                if(null != det_t && null != det_t.getName() && "" != det_t.getName()){
                                    eff_t.setDepartmentName(det_t.getName()); 
                                }
                                eff_t.setDepartmentId(eff_re_bo.getDepartmentId());
                            }
                            
                            //更加id查询对象
                            ee_t = employeDao.findOne(eff_re_bo.getEmployeId().longValue());
                            if(null != ee_t){
                                if(null != ee_t.getCode() && "" != ee_t.getCode()){
                                    eff_t.setEmployeCode(ee_t.getCode());
                                }
                                if(null != ee_t.getId()){
                                    eff_t.setEmployeId(ee_t.getId().intValue());
                                }
                                if(null != ee_t.getName() && "" != ee_t.getName() ){
                                    eff_t.setEmployeName(ee_t.getName());
                                }
                            }
                            
                            //保存数据
                            efficiencyDao.save(eff_t);
                            efficiency_list_t.add(eff_t);
                            //修改时保存修改记录
                            addProjectChangeUpdate(efficiency_old_temp,eff_t,user);
                            break;
                            
                        }
                    }
                }
                /*end*/ 
            }
            /*end*/
        }
        return efficiency_list_t;
    }

    /**
     * 封装添加 EfficiencyBo 方法
     * @param efficiencyBo
     * @param mapT
     * @param user 
     * @param efficiency_list_t 
     * @return 
     */
    public List<Efficiency> addEff(EfficiencyBo efficiencyBo,Map<String,Object> mapT, ShiroUser user, List<Efficiency> efficiency_list_t){
        //创建部门 Department  对象
        Department det = new Department();
        
        Date plan_date = null;
        Date end_date = null;
        Integer d_t = 0;
        
        EfficiencyRecordBo efficiencyRecordBo =null;
        
        //map中获取 project 及 list 数据
        Project project = (Project) mapT.get("project");
        List<Employe> list = (List<Employe>) mapT.get("list");
        
        if(null != efficiencyBo){
            //判断部门id是否为空
            if(null != efficiencyBo.getEfficiencyRecordBo() && efficiencyBo.getEfficiencyRecordBo().size() > 0){
                List<EfficiencyRecordBo> efficiencyRecordBoList = efficiencyBo.getEfficiencyRecordBo();
                for(int i=0 ; i<efficiencyRecordBoList.size() ;i++){
                    if(null != efficiencyRecordBoList.get(i) && ( null != efficiencyRecordBoList.get(i).getDepartmentId() 
                            || null != efficiencyRecordBoList.get(i).getEmployeId() 
                            || null != efficiencyRecordBoList.get(i).getPlanHours() 
                            ||null != efficiencyRecordBoList.get(i).getPlanBeginTime() 
                            || null != efficiencyRecordBoList.get(i).getPlanEndTime())  ){
                        
                        efficiencyRecordBo = efficiencyRecordBoList.get(i);
                        //创建Efficiency 对象
                        Efficiency efficiency =new Efficiency();
                        //添加创建时间  项目名称  及 及创建人名称
                        efficiency.setCreateTime(Calendar.getInstance().getTime());
                        if(null != efficiencyBo.getProjectNameBo() && "" != efficiencyBo.getProjectNameBo()){
                            efficiency.setProjectName(efficiencyBo.getProjectNameBo());
                        }
                        if(null != user && null != user.getName() && "" != user.getName()){
                            efficiency.setPm(user.getName());
                        }
                        //获取项目id —— ProjectId
                        if(null != project && null != project.getId()){
                            efficiency.setProjectId(project.getId().intValue());
                        }
                        //添加项目状态
                        if(null != project.getState()){
                            efficiency.setProjectState(project.getState());
                        }
                        
                        //从已经查询出来的员工集合中取得数据封装对象
                        if(null != list && list.size() > 0){
                            if(null != list.get(i) ){
                                if(null != list.get(i).getId()){
                                    efficiency.setEmployeId(list.get(i).getId().intValue());
                                }
                                if(null != list.get(i).getCode()){
                                    efficiency.setEmployeCode(list.get(i).getCode()); 
                                }
                                if(null != list.get(i).getName() && "" != list.get(i).getName()){
                                    efficiency.setEmployeName(list.get(i).getName());
                                }
                            }
                        }
                        
                        //判断部门id  根据部门id查询部门对象
                        if(null != efficiencyRecordBo.getDepartmentId() ){
                            det = departmentDao.findOne(efficiencyRecordBo.getDepartmentId().longValue());
                            if(null != det && null != det.getName() && "" != det.getName()){
                                efficiency.setDepartmentName(det.getName()); 
                                efficiency.setDepartmentId(det.getId().intValue());
                            }
                        }   
                        //添加计划时间   
                        if(null != efficiencyRecordBo.getPlanHours()) {
                            d_t= efficiencyRecordBo.getPlanHours();
                            efficiency.setPlanHours(d_t);
                        }
                        
                        //添加时间
                        if(null != efficiencyRecordBo.getPlanBeginTime() ){
                            plan_date = efficiencyRecordBo.getPlanBeginTime() ;
                            efficiency.setPlanBeginTime(plan_date);
                            efficiency.setActualBeginTime(plan_date);
                            efficiency.setOutputBeginTime(plan_date);
                        }  
                            
                        if(null != efficiencyRecordBo.getPlanEndTime()) {
                            end_date = efficiencyRecordBo.getPlanEndTime();
                            efficiency.setPlanEndTime(end_date);
                            efficiency.setActualEndTime(end_date);
                            efficiency.setOutputEndTime(end_date);
                        }  
                            
                        //保存efficiency对象
                        efficiencyDao.save(efficiency);
                        //添加 project_change信息
                        /*addProjectChange(new ArrayList<ProjectChange>(), efficiency);*/
                        //封装list数据
                        efficiency_list_t.add(efficiency);
                    }
                }
            }
        }
        return efficiency_list_t;
    }
    
    

    /***
     * 项目经理修改项目人员时 
     * 新增加的人员  记录 需要在  project_change表中增加新的记录
     * @param efficiency
     * @param user 
     */
    private void addFirstProjectChange(Efficiency efficiency, ShiroUser user) {
        ProjectChange projectChange_first = new ProjectChange();
        projectChange_first.setCreateTime(Calendar.getInstance().getTime());
        //添加项目id  相关开发人员id 及  操作人姓名
        if(null != efficiency.getId() ){
            projectChange_first.setEfficiencyId(efficiency.getId().intValue());
        }
        if(null != efficiency.getEmployeId() ){
            projectChange_first.setEmployeId(efficiency.getEmployeId().intValue());
        }
        if(null != efficiency.getProjectId() ){
            projectChange_first.setProjectId(efficiency.getProjectId());
        }
        if( null != user && null != user.name && "" != user.name){
            projectChange_first.setOptName(user.name);
        }
        //插入操作字段数据
        String after = addStrByRecord(efficiency);
        if(StringUtils.isNotBlank(after)){
            
            projectChange_first.setChangeAfter(after);
            projectChangeDao.save(projectChange_first);
        }
    }
    
    /**
     * 修改时 修改记录方法
     * @param efficiency_old_temp
     * @param efficiency_new
     * @param user
     */
    private void addProjectChangeUpdate(Efficiency efficiency_old_temp, Efficiency efficiency_new, ShiroUser user) {
        List<ProjectChange> projectChange_List = null;//创建项目修改记录对象
        if(null != efficiency_new && null != efficiency_new.getProjectId() && null !=  efficiency_new.getId()){
            ProjectChange projectChange_second = new ProjectChange();
            projectChange_second.setCreateTime(Calendar.getInstance().getTime());
            //添加项目id  相关开发人员id 及  操作人姓名
            if(null != efficiency_new.getId() ){
                projectChange_second.setEfficiencyId(efficiency_new.getId().intValue());
            }
            if(null != efficiency_new.getEmployeId() ){
                projectChange_second.setEmployeId(efficiency_new.getEmployeId().intValue());
            }
            if(null != efficiency_new.getProjectId() ){
                projectChange_second.setProjectId(efficiency_new.getProjectId());
            }
            if( null != user && null != user.name && "" != user.name){//操作者
                projectChange_second.setOptName(user.name);
            }
            
            //拼接修改时，新对象 字符串信息
            String after =addStrByRecord(efficiency_new);
            
            //根据projectid及效能主键id查询修改记录信息
            projectChange_List = projectChangeDao.findByProjectIdAndEfficiencyId(efficiency_new.getProjectId(),efficiency_new.getId().intValue());
            //存在记录信息则已经添加过记录信息否则需要新添加修改记录
            if(null != projectChange_List && projectChange_List.size() > 0){
                if(null != projectChange_List.get(0)){
                    ProjectChange projectChange_one = projectChange_List.get(0);
                    //如果projectChange_one.getChangeAfter() 则是 已经存在一条修改记录 信息 
                    if( StringUtils.isNotBlank(projectChange_one.getChangeAfter()) && StringUtils.isNotBlank(after) && !after.equals(projectChange_one.getChangeAfter())){
                        //判断 新添加的修改记录信息after  
                        //是否和 上次修改的记录信息 projectChange_one.getChangeAfter()  
                        //是否  相同  相同则代表无修改任何信息     则 不 需要重新添加修改记录
                        projectChange_second.setChangeBefore(projectChange_one.getChangeAfter());//将修改记录放到修改前 
                        projectChange_second.setChangeAfter(after);//set修改之后的信息
                        projectChangeDao.save(projectChange_second);
                    }
                }
                //projectChange无修改数据代表  添加的  人员 一直没有改动 计划 
            }else{
                if(null != efficiency_old_temp) {
                    //拼接修改时，老对象 字符串信息
                    String before =  addStrByRecord(efficiency_old_temp);
                    if(StringUtils.isNotBlank(before)&& StringUtils.isNotBlank(after) && !before.equals(after)){
                        projectChange_second.setChangeAfter(after);
                        projectChange_second.setChangeBefore(before);
                        projectChangeDao.save(projectChange_second);
                    }
                    
                }
            }
        }
    }
    
    /**
     * 拼接字符串
     * @param record
     * @return
     */
    private String addStrByRecord(Efficiency record) {
        //拼接修改时，新对象 字符串信息
        String str = "";
 
        if(null != record){
            //人员名称
            if(StringUtils.isNotBlank(record.getEmployeName())){
                str += record.getEmployeName()+",";
            }else{
                str += "-,";
            }
            //部门名称
            if(StringUtils.isNotBlank(record.getDepartmentName())){
                str += record.getDepartmentName()+",";
            }else{
                str += "-,";
            }
            //计划时间
            if(null != record.getPlanBeginTime() ){
                str += "周期"+new SimpleDateFormat("yyyy-MM-dd").format(record.getPlanBeginTime())+"至";
            }else{
                str += "周期-至";
            }
            //结束时间
            if(null != record.getPlanEndTime()){
                str+= new SimpleDateFormat("yyyy-MM-dd").format(record.getPlanEndTime())+";";
            }else{
                str += "-;";
            }
            //工时
            if(null != record.getPlanHours()){
                str += "工时"+record.getPlanHours() +"小时";
            }else{
                str += "工时0小时";
            }
        }
        return str;
    }

    /**
     * 删除对象的修改记录
     * @param user 
     * @param List<Efficiency> del_list
     */
    private void delProjectChange(List<Efficiency> del_list, ShiroUser user) {
        if(null != del_list && del_list.size() > 0){
            for(Efficiency temp : del_list){
                if(null != temp ){
                    ProjectChange projectChange_three = new ProjectChange();
                    projectChange_three.setCreateTime(Calendar.getInstance().getTime());
                    //添加项目id  相关开发人员id 及  操作人姓名
                    if(null != temp.getId() ){
                        projectChange_three.setEfficiencyId(temp.getId().intValue());
                    }
                    if(null != temp.getEmployeId() ){
                        projectChange_three.setEmployeId(temp.getEmployeId().intValue());
                    }
                    if(null != temp.getProjectId() ){
                        projectChange_three.setProjectId(temp.getProjectId());
                    }
                    if( null != user && null != user.name && "" != user.name){//操作者
                        projectChange_three.setOptName(user.name);
                    }
                    String bef =addStrByRecord(temp);
                    
                    projectChange_three.setChangeBefore(bef);
                    projectChangeDao.save(projectChange_three);
                }
            }
        }
        
    }

    /**
     * 封装数据   用于添加 及编辑  时
     * @param efficiencyBo
     * @param project
     * @return
     */
    public  Map<String,Object> addProject(EfficiencyBo efficiencyBo,Project project){
        //创建map对象用于返回值使用
        Map<String,Object> map = new HashMap<String,Object>();
        //创建list 存放 Employe 对象
        List<Employe> list = new ArrayList<Employe>();
        Employe  ee = new Employe();
        
        String s = "";
        Date dateBegin = null;
        Date dateEnd = null;
        Date dateBeginTemp = null;
        Date dateEndTemp = null;
        //拼字符串   
        if(null != efficiencyBo ){
            if(null != efficiencyBo.getEfficiencyRecordBo() && efficiencyBo.getEfficiencyRecordBo().size()>0){
                List<EfficiencyRecordBo> efficiencyRecordBo = efficiencyBo.getEfficiencyRecordBo();
                for(EfficiencyRecordBo t : efficiencyRecordBo){
                    if(null != t  && null != t.getEmployeId()){
                        //更加id查询  Employe 对象
                        ee = employeDao.findOne(t.getEmployeId().longValue());
                        list.add(ee);//放入list
                        if(null != ee && null != ee.getName() && "" != ee.getName()){
                            s=s+ee.getName()+",";
                        }
                    }else{
                        list.add(null);//放入list
                    }
                    
                    // 比较开始时间
                    if(null !=t && null != t.getPlanBeginTime()){
                        dateBegin = t.getPlanBeginTime();
                        if(null != dateBeginTemp){
                            if( dateBeginTemp.getTime() > dateBegin.getTime() ){
                                dateBeginTemp = dateBegin;
                            }
                        }else{
                            dateBeginTemp = dateBegin;
                        }
                    }
                    
                    //比较结束时间
                    if(null !=t && null != t.getPlanEndTime()){
                        dateEnd = t.getPlanEndTime();
                        if(null != dateEndTemp){
                            if(dateEnd.getTime() > dateEndTemp.getTime() ){
                                dateEndTemp = dateEnd;
                            }
                        }else{
                            dateEndTemp = dateEnd;
                        }
                    }
                }
                //去掉 字符串最后一个逗号
                if(null != s && "" != s){
                    s=s.substring(0,s.length()-1);
                }
            }
            if(null != efficiencyBo.getProjectNameBo() && "" != efficiencyBo.getProjectNameBo()){
                project.setName(efficiencyBo.getProjectNameBo());
            }
        }
        //对象保存字符串信息
        if(null != s && "" != s){
            project.setProjectPersonnel(s);
        }
        //开始时间
        if(null != dateBeginTemp ){
            project.setProjectBeginTime(dateBeginTemp);
        }
        //结束时间
        if(null != dateEndTemp){
            project.setProjectEndTime(dateEndTemp);
        }
        //放入map中返回
        map.put("list", list);
        map.put("project", project);
        return  map ;
    }
    

    public Page<Efficiency> findEfficiencyAll(final EfficiencyQuery q) {
        Specification<Efficiency> sp = new Specification<Efficiency>() {

            @Override
            public Predicate toPredicate(Root<Efficiency> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotEmpty(q.getEmployeName())) {
                    list.add(cb.equal(root.get("employeName").as(String.class), q.getEmployeName()));
                }
                if (StringUtils.isNotEmpty(q.getEmployeCode())) {
                    list.add(cb.equal(root.get("employeCode").as(String.class), q.getEmployeCode()));
                }
                if (StringUtils.isNotEmpty(q.getProjectName())) {
                    list.add(cb.equal(root.get("projectName").as(String.class), q.getProjectName()));
                }
                if (q.getPlanHours() != null) {
                    list.add(cb.equal(root.get("planHours").as(Integer.class), q.getPlanHours()));
                }
                if (q.getActualHours() != null) {
                    list.add(cb.equal(root.get("actualHours").as(Integer.class), q.getActualHours()));
                }
                if (q.getProjectId() != null) {
                    list.add(cb.equal(root.get("projectId").as(Integer.class), q.getProjectId()));
                }
                if (q.getEmployeId() != null) {
                    list.add(cb.equal(root.get("employeId").as(Integer.class), q.getEmployeId()));
                }
                if (q.getProjectState() != null) {
                    list.add(cb.equal(root.get("projectState").as(Integer.class), q.getProjectState()));
                }
                if (q.getOutputHours() != null) {
                    list.add(cb.equal(root.get("outputHours").as(Integer.class), q.getOutputHours()));
                }
                if (StringUtils.isNotEmpty(q.getPm())) {
                    list.add(cb.equal(root.get("pm").as(String.class), q.getPm()));
                }
                
                if(q.getPlanBeginTime()!=null){
                    list.add(cb.greaterThanOrEqualTo(root.get("planBeginTime").as(Date.class), q.getPlanBeginTime()));
                }
                if(q.getPlanEndTime()!=null){
                    list.add(cb.lessThanOrEqualTo(root.get("planEndTime").as(Date.class), q.getPlanEndTime()));
                }
                
                Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(q.getSort())) {
                    query.orderBy(cb.desc(root.get(q.getSort())));
                }
                return query.getGroupRestriction();
            }
        };
        PageRequest pageRequest = null;
        if (q.getPageIndex() != null && q.getPageSize() != null) {
            pageRequest = new PageRequest(q.getPageIndex(), q.getPageSize());
        }
        Page<Efficiency> pageList = efficiencyDao.findAll(sp, pageRequest);
        return pageList;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Efficiency findEfficiencyById(Long id) {
        Efficiency  efficiency = efficiencyDao.findOne(id);
        return efficiency;
    }

    /**
     * 录入工时 解析方法
     * @param hours
     * @param id
     * @param i 
     * @return
     */
    public Efficiency addAhours(int hours, Long id, int i) {
        Efficiency record = new Efficiency();
        Efficiency efficiency = efficiencyDao.findOne(id);
        if(null != efficiency ){
            
            if(1 == i ){
                efficiency.setActualHours(hours);
                efficiencyDao.save(efficiency);
                
                record.setActualHours(hours);
                record.setActualBeginTime(efficiency.getActualBeginTime());
                record.setActualEndTime(efficiency.getActualEndTime());
                
                /*if(null != efficiency.getActualEndTime() && null != efficiency.getActualBeginTime() ){
                    Date x= add_date(efficiency.getActualBeginTime(),hours);
                    efficiency.setActualEndTime(x);
                    record.setActualEndTime(x);
                    efficiencyDao.save(efficiency);
                }*/
            }else if(2 == i ){
                efficiency.setOutputHours(hours);
                efficiency.setProjectState(1);
                efficiencyDao.save(efficiency);
                
                record.setOutputHours(hours);
                record.setOutputBeginTime(efficiency.getOutputBeginTime());
                record.setOutputEndTime(efficiency.getOutputEndTime());
               
                /*if(null != efficiency.getOutputEndTime() && null != efficiency.getOutputBeginTime()){
                    Date x= add_date(efficiency.getOutputBeginTime(),hours);
                    efficiency.setOutputEndTime(x);
                    efficiency.setProjectState(1);
                    record.setOutputEndTime(x);
                    efficiencyDao.save(efficiency);
                }*/
                //调用更新项目状态
                if(null != efficiency.getProjectId()){
                    List<Efficiency> listEfficiency = efficiencyDao.findByProjectId(efficiency.getProjectId());
                    if(null != listEfficiency && listEfficiency.size() > 0){
                        int a = 0;
                        for(Efficiency tem_ : listEfficiency){
                            if(null != tem_ && null != tem_.getProjectState() && 0 == tem_.getProjectState()){
                                a= a+1;
                            }
                        }
                        if(a==0){
                            Project project_ = projectDao.findOne(efficiency.getProjectId().longValue());
                            project_.setState(1);//项目状态为已完成
                            projectDao.save(project_);
                        }
                    }
                }
            }
        }
        return record;
    }

    /***
     * 根据员工的编号和时间查找出该员工本月的效能，然后计算绩效能的分数
     * @param h
     * @param hours
     * @return
     */
    public List<Efficiency> findEfficiencyByEmployeCodeAndTime(final Long employeId ,final Date time){
    	
    	Specification<Efficiency> sp=new Specification<Efficiency>() {

			@Override
			public Predicate toPredicate(Root<Efficiency> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				if(employeId!=null){
					list.add(cb.equal(root.get("employeId").as(Integer.class),employeId));
				}
				if(time !=null){
					SimpleDateFormat f=new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("planBeginTime").as(String.class), 1,7),f.format(time)));
				}
				Predicate[] ps=new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				return query.getGroupRestriction();
			}
		};
		List<Efficiency> efs=efficiencyDao.findAll(sp);
    	return efs;
    }
    
    private Date add_date(Date h, int hours) {
        Long a = h.getTime();
        int t = hours/8;
        Long b = a+((long)t*1000*24*60*60);
        Date date = new Date(b);
        return date;
    }

    /**
     * 更加项目删除项目
     * @param projectId
     */
    public void deleteProjectAndEfficiencyByProjectId(Long projectId) {
        Project project = projectDao.findOne(projectId);
        if(null != project){
            List<Efficiency> efficiencyList = efficiencyDao.findByProjectId(projectId.intValue());
            if(null != efficiencyList && efficiencyList.size() > 0){
                efficiencyDao.delete(efficiencyList);
            }
            projectDao.delete(project);
        }
    }


}
