package com.zdm.test.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {

	static int parties=6;
	static CyclicBarrier cb = new CyclicBarrier(parties);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Runnable r = new Runnable() {

			public void run() {
				try {
					Thread.sleep((long) (Math.random() * 10000));
					System.out.println("first" + cb.getNumberWaiting());
					cb.await();
					Thread.sleep((long) (Math.random() * 10000));
					System.out.println("second" + cb.getNumberWaiting());
					cb.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}

			}
		};
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < parties; i++) {
			es.execute(r);
		}
		es.shutdown();

	}

}
