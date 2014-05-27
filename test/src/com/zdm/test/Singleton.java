package com.zdm.test;

public class Singleton {

	private Singleton() {
			
	}
	
	private static Singleton ins=new Singleton();
	
	public static Singleton getIns(){
		return ins;
	}
}
