package com.zdm.test.concurrent;

import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedList<String> linkedL = new LinkedList<String>();

		for (int i = 0; i < 10; i++) {
			linkedL.add("str" + i);
		}

		System.out.println(linkedL.pop());
		System.out.println(linkedL.size());
		System.out.println(linkedL.get(0));

		linkedL.add(0, "first");
		System.out.println(linkedL.get(0));
		System.out.println(linkedL.size());

		Stack<String> s = new Stack<String>();
		for (int i = 0; i < 10; i++) {
			s.add("str" + i);
		}
		System.out.println(s.peek());
		System.out.println(s.search("str0"));
		System.out.println(s.firstElement());
		System.out.println(s.get(9));

		System.out.println("=====================");
		BlockingQueue<String> bq = new ArrayBlockingQueue<String>(5);
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println(bq.size());
				if (bq.size() == 5) {
					System.out.println(bq.take());
				}
				bq.put("str" + i);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(bq.size());
	}

}
