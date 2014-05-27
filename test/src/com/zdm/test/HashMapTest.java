package com.zdm.test;

import java.util.Date;
import java.util.HashMap;

public class HashMapTest {

	
	private static HashMap<Integer, Integer> m=new HashMap<Integer, Integer>(50001);
	private int num=100000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Date sd=new Date();
		for(int i=0;i<4;i++){
			new Thread(new HashMapTest().new MyThread()).start();
		}
		System.out.println("total time="+(new Date().getTime()-sd.getTime()));
		
	}
	
	class MyThread implements Runnable{

		public void run() {			
			for(int i=0;i<num;i++){
				m.put(i, i);
			}
//			System.out.println(Thread.currentThread().getName()+"=="+m.size());
			
		}
		
	}

}
