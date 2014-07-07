package com.zdm.test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyThread extends Thread {
	
	class innerClass{
		
	}

	private int index;

	@Override
	public void run() {
		try {
			System.out.println("[" + this.index + "] start....");
			Thread.sleep((int) (Math.random() * 10000));
			System.out.println("[" + this.index + "] end.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MyThread(int index) {
		super();
		this.index = index;
	}

	public static Callable getCallable(final int id) {
		return new Callable() {

			public Object call() throws Exception {
				try {
					System.out.println("[" + id + "] start....");
					Thread.sleep((int) (Math.random() * 10000));
					System.out.println("[" + id + "] end.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return id+"ffffffff";
			}
		};
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(2);
		List<Future<String>> resultList = new ArrayList<Future<String>>();

		for (int i = 0; i < 10; i++) {
			// es.execute(new MyThread(i));
			Future<String> future = (Future<String>) es.submit(getCallable(i));
			resultList.add(future);
		}
		System.out.println("submit finish");
		es.shutdown();

		// 遍历任务的结果
		for (Future<String> fs : resultList) {
			try {
				System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// executorService.shutdownNow();
				e.printStackTrace();
				return;
			}
		}
	}

}
