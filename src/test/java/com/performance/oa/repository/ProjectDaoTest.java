package com.performance.oa.repository;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.performance.oa.entity.Project;
import com.performance.oa.query.ProjectQuery;
import com.performance.oa.service.project.ProjectService;

public class ProjectDaoTest extends BaseTest{
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	ProjectService projectService;
	
	@Test
	public void test(){
		final ProjectQuery d=new ProjectQuery();
		List<Project> list=projectService.find(d);
		for(Project p:list){
			System.out.println("name>>>>."+p.getName());
		}
	}
}
