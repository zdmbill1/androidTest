package com.zdm.test;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadTest {

	public static void main(String[] args) {
		ErrHandler handle = new ErrHandler();
		ThreadA a = new ThreadA();
		MyThread t1 = new MyThread();
		
		t1.setUncaughtExceptionHandler(handle);// 加入定义的ErrHandler
		t1.start();

	}

}

/**
 * 自定义的一个UncaughtExceptionHandler
 */
class ErrHandler implements UncaughtExceptionHandler {
	/**
	 * 这里可以做任何针对异常的处理,比如记录日志等等
	 */
	public void uncaughtException(Thread a, Throwable e) {
		System.out.println("=========");
		System.out.println(e.getMessage());
		System.out.println("This is:" + a.getName() + ",Message:"
				+ e.getMessage());
		e.printStackTrace();
	}
}

/**
 * 拥有UncaughtExceptionHandler的线程
 */
class ThreadA extends Thread {

	public ThreadA() {

	}

	public void run() {
		System.out.println(1 / 0);
		// double i = 12 / 0;// 抛出异常的地方
	}

}