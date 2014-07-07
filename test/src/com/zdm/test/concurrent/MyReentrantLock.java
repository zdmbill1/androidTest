package com.zdm.test.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock extends Thread {

	private int id;
	private ReentrantLock rLock;

	public MyReentrantLock(int id, ReentrantLock rLock) {
		super();
		this.id = id;
		this.rLock = rLock;
	}

	@Override
	public void run() {
		try {
			rLock.lock();
			System.out.println("id=" + id + "获得锁");
			Thread.sleep((int) (Math.random() * 10000));

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rLock.unlock();
			System.out.println("id=" + id + "释放锁");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		ReentrantLock lock = new ReentrantLock();
		for (int i = 0; i < 10; i++) {
			es.execute(new MyReentrantLock(i, lock));
		}

	}

}
