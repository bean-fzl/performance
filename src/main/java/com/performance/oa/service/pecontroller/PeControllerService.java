package com.ync365.oa.service.pecontroller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ync365.oa.entity.PeController;
import com.ync365.oa.repository.PeControllerDao;

@Component
@Transactional
public class PeControllerService {
    @Autowired
    private PeControllerDao peControllerDao;

    /**
     * 确定是否处理本部门绩效
     * @Title: checkIsProcessed
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年11月28日 下午5:21:18
     * @version: 
     *
     * @param departmentName
     * @param date
     * @return true 可以处理  false 已经处理
     *
     */
    public synchronized Boolean checkIsProcessed(Long departmentId, String departmentName, String date) {
        Boolean result = false;
        PeController pe = peControllerDao.findByPeDateDepartment(departmentName + "_" + date);
        if (pe == null) {
            pe = new PeController();
            pe.setDepartmentId(departmentId);
            pe.setCreateTime(Calendar.getInstance().getTime());
            pe.setUpdateTime(Calendar.getInstance().getTime());
            pe.setPeDateDepartment(departmentName + "_" + date);
            pe.setState("0");
            peControllerDao.save(pe);
            result = true;
        } else {
            if (!"1".equals(pe.getState())) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 处理完成，更改状态
     * @Title: updateState
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年11月28日 下午5:26:41
     * @version: 
     *
     * @param departmentName
     * @param date
     *
     */
    public void updateState(String departmentName, String date) {
        PeController pe = peControllerDao.findByPeDateDepartment(departmentName + "_" + date);
        pe.setState("1");
        pe.setUpdateTime(Calendar.getInstance().getTime());
        peControllerDao.save(pe);
    }

    /**
     * 部门ID查询所有已产生的部门绩效
     * @Title: findByDepartmentId
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年12月2日 下午5:33:15
     * @version: 
     *
     * @param departmentId
     * @return
     *
     */
    public Page<PeController> findByDepartmentId(Long departmentId) {
        return peControllerDao.findByDepartmentId(departmentId,
                new PageRequest(0, Integer.MAX_VALUE, new Sort(Direction.DESC, "createTime")));
    }
}
