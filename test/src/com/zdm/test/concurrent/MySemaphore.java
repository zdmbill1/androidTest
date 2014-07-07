package com.zdm.test.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MySemaphore extends Thread {

	private Semaphore position;
	private int index;

	public MySemaphore(Semaphore position, int index) {
		this.position = position;
		this.index = index;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();

		Semaphore pos = new Semaphore(2);
		for (int i = 0; i < 10; i++) {
			es.execute(new MySemaphore(pos, i));
		}
		System.out.println("submit finish");
		es.shutdown();
	}

	@Override
	public void run() {
		if (position.availablePermits() > 0) {
			System.out.println("index="+index+"还有位置");
		} else {
			System.out.println("index="+index+"木有位置了");
		}
		try {
			position.acquire();
			System.out.println("index="+index+"开始使用位置");
			Thread.sleep((int) (Math.random() * 10000));
			System.out.println("index="+index+"使用位置完毕");
			position.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
