package com.zdm.test.sort;

import java.util.Date;

public class BubbleSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] test = { 2, 6, 5, 8, 7, 9, 10, 256, 248, 14, 99, 134, 555, 3, 87,
				69 };
		Date sd = new Date();
		bubbleSort(test);
		Date ed = new Date();
		System.out.println(ed.getTime() - sd.getTime() + "ms");
		for (int i = 0; i < test.length; i++) {
			System.out.print(test[i] + ",");
		}

	}

	public static void bubbleSort(int[] nums) {
		int length = nums.length;
		int temp;
		for (int i = 1; i < length; i++) {
			for (int j = 0; j < length - i; j++) {
				if (nums[j] > nums[j + 1]) {
					// 交换
					temp = nums[j];
					nums[j] = nums[j + 1];
					nums[j + 1] = temp;
				} else {
					continue;
				}
			}
		}
	}

}
