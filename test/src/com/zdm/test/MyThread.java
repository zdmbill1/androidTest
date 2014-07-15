package com.zdm.test;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author bill
 *2个不同的实例就可以同时调用synchronized方法,要保证是同一个实例
 */
public class MyThread extends Thread {
	private int id = 0;
	private Common common;

	public MyThread(String name, int no, Common object) {
		super(name);
		common = object;
		id = no;
	}

	public MyThread(){
		
	}
	public void run() {
		System.out.println("Running Thread" + this.getName());
		
			if (id == 0) {
//			Common common = new Common();
				common.synchronizedMethod1();		
				System.out.println(1/0);
			} else {
				common.synchronizedMethod2();
			}
		
	}

	public static void main(String[] args) {
		Common c = new Common();
		MyThread t1 = new MyThread("MyThread-1", 0, c);
		MyThread t2 = new MyThread("MyThread-2", 1, c);
		myUn un=new myUn();
		Thread.setDefaultUncaughtExceptionHandler(un);
//		t1.setUncaughtExceptionHandler(un);
		t1.start();
		t2.start();
		
		
	}
}

class myUn implements UncaughtExceptionHandler{

	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("=========");
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	
}