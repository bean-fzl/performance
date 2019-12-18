package com.ync365.oa.service.performanceevaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.entity.PerformanceEvaluation;
import com.ync365.oa.query.PerformanceEvaluationQuery;
import com.ync365.oa.repository.PerformanceEvaluationDao;

@Component
@Transactional
public class PerformanceEvaluationService {
    @Autowired
    private PerformanceEvaluationDao performanceEvaluationDao;
    private Logger log = LoggerFactory.getLogger(PerformanceEvaluationService.class);

    /**
     * 查询绩效
     * @Title: find
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年11月28日 下午3:43:08
     * @version: 
     *
     * @param q
     * @return
     *
     */
    public Page<PerformanceEvaluation> find(final PerformanceEvaluationQuery q) {
        Specification<PerformanceEvaluation> sp = new Specification<PerformanceEvaluation>() {

            @Override
            public Predicate toPredicate(Root<PerformanceEvaluation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (q.getId() != null) {
                    list.add(cb.equal(root.get("id").as(Long.class), q.getId()));
                }
                if (StringUtils.isNotEmpty(q.getBeEvaluatedName())) {
                    list.add(cb.equal(root.get("beEvaluatedName").as(String.class), q.getBeEvaluatedName()));
                }
                if (StringUtils.isNotEmpty(q.getDepartmentName())) {
                    list.add(cb.equal(root.get("departmentName").as(String.class), q.getDepartmentName()));
                }
                if (StringUtils.isNotEmpty(q.getTotalGrade())) {
                    list.add(cb.equal(root.get("totalGrade").as(String.class), q.getTotalGrade()));
                }
                if (q.getEfficiencyScore() != null) {
                    list.add(cb.equal(root.get("efficiencyScore").as(Double.class), q.getEfficiencyScore()));
                }
                if (q.getSpecialtyScore() != null) {
                    list.add(cb.equal(root.get("specialtyScore").as(Double.class), q.getSpecialtyScore()));
                }
                if (q.getLeaderAssessmentScore() != null) {
                    list.add(cb.equal(root.get("leaderAssessmentScore").as(Double.class),
                            q.getLeaderAssessmentScore()));
                }
                if (q.getSatisfactionScore() != null) {
                    list.add(cb.equal(root.get("satisfactionScore").as(Double.class), q.getSatisfactionScore()));
                }
                if (q.getTotalScore() != null) {
                    list.add(cb.equal(root.get("totalScore").as(Double.class), q.getTotalScore()));
                }
                if (q.getCreateTime() != null) {
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
                    list.add(cb.equal(cb.substring(root.get("createTime").as(String.class), 1, 7),
                            f.format(q.getCreateTime())));
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
        Page<PerformanceEvaluation> pages = null;
        pages = performanceEvaluationDao.findAll(sp, pageRequest);
        return pages;
    }

    /**
     * 查询单个
     * @Title: findOne
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年11月28日 下午3:48:10
     * @version: 
     *
     * @param id
     * @return
     *
     */
    public PerformanceEvaluation findOne(Long id) {
        return performanceEvaluationDao.findOne(id);
    }

    /**
     * 保存更新
     * @Title: update
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年11月28日 下午3:49:00
     * @version: 
     *
     * @param entity
     * @return
     *
     */
    public PerformanceEvaluation update(PerformanceEvaluation entity) {
        entity.setUpdateTime(Calendar.getInstance().getTime());
        return performanceEvaluationDao.save(entity);
    }

    /**
     * 导出excel
     * @Title: export
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年12月7日 上午10:15:37
     * @version: 
     *
     * @param q
     * @return
     *
     */
    public String export(PerformanceEvaluationQuery q,String pathPre) {
        q.setPageIndex(null);
        q.setPageSize(null);
        Page<PerformanceEvaluation> page = find(q);
        List<PerformanceEvaluation> list = page.getContent();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String filePath = Paths.get(pathPre, "jixiao" + f.format(Calendar.getInstance().getTime()) + ".xls").toString();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        Long sheetNum = 60000l;
        Long num = (long) Math.ceil((double) list.size() / sheetNum);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM");
        for (int j = 0; j < num; j++) {
            HSSFSheet sheet = hssfWorkbook.createSheet("sheet" + j);
            HSSFRow hssftitle = sheet.createRow(0);
            String[] titles = { "绩效总评ID", "被评价者", "部门", "效能", "专业", "上级评价", "客户满意", "总分", "总评分级", "时间" };
            for (int i = 0; i < titles.length; i++) {
                HSSFCell c = hssftitle.createCell(i);
                c.setCellValue(titles[i]);
            }
            for (int i = 0; i < Math.min(list.size() - j * sheetNum, sheetNum); i++) {
                HSSFRow hssfrow = sheet.createRow(i + 1);
                hssfrow.createCell(0).setCellValue(list.get((int) (j * sheetNum + i)).getId());
                hssfrow.createCell(1).setCellValue(list.get((int) (j * sheetNum + i)).getBeEvaluatedName() != null
                        ? list.get((int) (j * sheetNum + i)).getBeEvaluatedName() : "");
                hssfrow.createCell(2).setCellValue(list.get((int) (j * sheetNum + i)).getDepartmentName() != null
                        ? list.get((int) (j * sheetNum + i)).getDepartmentName() : "");
                hssfrow.createCell(3).setCellValue(list.get((int) (j * sheetNum + i)).getEfficiencyScore() != null
                        ? list.get((int) (j * sheetNum + i)).getEfficiencyScore() : 0);
                hssfrow.createCell(4).setCellValue(list.get((int) (j * sheetNum + i)).getSpecialtyScore() != null
                        ? list.get((int) (j * sheetNum + i)).getSpecialtyScore() : 0);
                hssfrow.createCell(5).setCellValue(list.get((int) (j * sheetNum + i)).getLeaderAssessmentScore() != null
                        ? list.get((int) (j * sheetNum + i)).getLeaderAssessmentScore() : 0);
                hssfrow.createCell(6).setCellValue(list.get((int) (j * sheetNum + i)).getSatisfactionScore() != null
                        ? list.get((int) (j * sheetNum + i)).getSatisfactionScore() : 0);
                hssfrow.createCell(7).setCellValue(list.get((int) (j * sheetNum + i)).getTotalScore() != null
                        ? list.get((int) (j * sheetNum + i)).getTotalScore() : 0);
                hssfrow.createCell(8).setCellValue(list.get((int) (j * sheetNum + i)).getTotalGrade() != null
                        ? list.get((int) (j * sheetNum + i)).getTotalGrade() : "");
                hssfrow.createCell(9).setCellValue(list.get((int) (j * sheetNum + i)).getCreateTime()!=null?f1.format(list.get((int) (j * sheetNum + i)).getCreateTime()):"");
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            hssfWorkbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            log.error("export", e);
        } catch (IOException e) {
            log.error("export", e);
        } finally {
            try {
                fileOutputStream.close();
                hssfWorkbook.close();
            } catch (IOException e) {
                log.error("export close", e);
            }
        }
        return filePath;
    }
    public void delete(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
