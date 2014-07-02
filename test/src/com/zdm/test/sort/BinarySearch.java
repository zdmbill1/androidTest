package com.zdm.test.sort;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class BinarySearch {

	static int count = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] tmp = new int[5];
		for (int i = 0; i < 5; i++) {
			tmp[i] = i * 2;
		}

		System.out.println(find(tmp, 8));
		System.out.println("count=" + count);
		
		for (int n : tmp) {
			// System.out.print(n + ",");
		}
//		long startTime = System.currentTimeMillis();
////		System.gc();
//		long startMem = getMemory();
//		System.out.println("start");
//		// System.out.println(find(tmp, 3, 0, tmp.length));
//		System.out.println(find(tmp, 2));
//		System.out.println("count=" + count);
//
//		for (int i = 0; i < 1000000; i++) {
//			find(tmp, 2);
//		}
//		long endTime = System.currentTimeMillis();
//		// System.gc();
//		long endMem = getMemory();
//		System.out.println("方法1耗时：" + (endTime - startTime) + "ms,消耗内存："
//				+ (endMem - startMem) + "byte");
//		count = 0;
//		System.out.println(find(tmp, 2, 0, tmp.length));
//		System.out.println("count=" + count);
//		for (int i = 0; i < 1000000; i++) {
//			find(tmp, 2, 0, tmp.length);
//		}
//		System.out.println("方法2耗时：" + (System.currentTimeMillis() - endTime)
//				+ "ms,消耗内存：" + (getMemory() - endMem) + "byte");
	}

	public static int find(int[] array, int num) {
		int start = 0, end = array.length, mid = (start + end) / 2;
		if (array[start] == num) {
			return start;
		} else if (array[end - 1] == num) {
			return end-1;
		}

		while (start < mid) {
			count = count + 1;
			// System.out
			// .println("start=" + start + " end=" + end + " mid=" + mid);
			if (num == array[mid]) {
				return mid;
			} else if (num < array[mid]) {
				end = mid;
				mid = (start + end) / 2;
			} else {
				start = mid;
				mid = (start + end) / 2;
			}
		}

		return -1;

	}

	/**
	 * @param array
	 * @param num
	 * @param start
	 * @param end
	 * @return 用的递归，要反复传array,效率应该有待提高?猜测
	 */
	public static int find(int[] array, int num, int start, int end) {

		count = count + 1;
		// if (array[start] == num) {
		// return start;
		// } else if (array[end-1] == num) {
		// return end;
		// }
		int mid = (start + end) / 2;
		// int midNum = array[mid];

		if ((end - start) / 2 > 0) {
			if (num == array[mid]) {
				return mid;
			} else if (num < array[mid]) {
				return find(array, num, start, mid);
			} else {
				return find(array, num, mid, end);
			}
		}
		return -1;
	}

	/**
	 * @return	单位是byte
	 * 都不准确
	 */
	private static long getMemory() {
//		Runtime runtime = Runtime.getRuntime();
//		return runtime.totalMemory() - runtime.freeMemory();
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		return osmb.getTotalPhysicalMemorySize() - osmb.getFreePhysicalMemorySize();
	}
}
