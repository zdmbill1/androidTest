package com.zdm.test.sort;

import java.util.Date;

public class DirectSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] test = { 21, 63, 45, 8, 7, 9, 10, 256, 248, 14, 99, 134, 555, 3,87, 69 };
		Date sd = new Date();
//		sortDirect(test, test.length);
		insertSort(test);
		Date ed = new Date();
		System.out.println(ed.getTime() - sd.getTime() + "ms");
		for (int i = 0; i < test.length; i++) {
			System.out.print(test[i] + ",");
		}

	}

	/**
	 * 直接插入排序
	 * 
	 * @param nums
	 * @param maxLen
	 */
	public static void sortDirect(int[] nums, int maxLen) {
		maxLen = maxLen - 1;
		int temp, j = 0;
		for (int i = 0; i < maxLen; i++) {
			temp = nums[i];
			if (nums[i + 1] < temp) {
				nums[i] = nums[i + 1];
				nums[i + 1] = temp;
				for (j = i; j > 0 && nums[j - 1] > nums[j]; j--) {
					temp = nums[j];
					nums[j] = nums[j - 1];
					nums[j - 1] = temp;
				}
				// nums[j] = temp;
			}
		}
	}

	public static void insertSort(int[] elements) {
		for (int i = 1; i < elements.length; i++) {
			int j = -1;
			while (j <= i && elements[i] > elements[++j])
				;// 找到element[i]应该摆放的位置，此处可以利用查找算法进行优化
			if (j < i) {
				// 将j之后的数据移动一位，然后把elements[i]移动到j处
				int temp = elements[i];
				for (int k = i - 1; k >= j; k--) {
					elements[k + 1] = elements[k];
				}
				elements[j] = temp;
			}
		}
	}
}
