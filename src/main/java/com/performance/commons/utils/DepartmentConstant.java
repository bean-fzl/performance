package com.performance.commons.utils;

public class DepartmentConstant {
	
	
	public static enum DeptType{
		/**
		 * 1、行政部 2、政治部 3 财务部 4 人事部 5 安全部 6 技术部
		 */
		PRODUCT(1,"行政部"),
		POLITICAL(2,"政治部"),
		FINANCE(3,"财务部"),
		PERSONNEL(4,"人事部"),
		SAFETY(5,"安全部"),
		TECHNICAL(6,"技术部"),
		OTHER(-1,"其它");
		
		private final int value;
		private final String content;
		
		private DeptType(int value ,String content){
			this.content=content;
			this.value=value;
		}
		public int v(){
			return this.value;
		}
		public String c(){
			return this.content;
		}
	}
	public static void main(String[] args) {
		String str=DepartmentConstant.DeptType.OTHER.c();
		System.out.println(str);
	}
}
