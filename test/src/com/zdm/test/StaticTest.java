package com.zdm.test;

public class StaticTest {

	{
		System.out.println("init");
	}
	
	static{
		a=5;
		System.out.println("static init");
		
	}
	
	private static int a ;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StaticTest st=new StaticTest();
		System.out.println(StaticTest.a);
		System.out.println(st.a);
	}

}
