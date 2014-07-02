package com.zdm.test.sort;

public class InsertSort {

	/**
	 * 插入排序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] tmp = { 5, 2, 8, 1, 3, 6, 11 };

//		int count=0;
//		for (int i = 1; i < tmp.length; i++) {
//			int t = tmp[i];
//			for (int j = i; j > 0 && tmp[j - 1] > t; j--) {
//				tmp[j] = tmp[j - 1];
//				tmp[j - 1] = t;
//				count++;
//				System.out.print("swap count="+count+"|| ");
//				printInts(tmp);	
//			}			
//		}
		
		insertSort(tmp);
		printInts(tmp);
	}

	public static void insertSort(int[] arrays){
		for (int i = 1; i < arrays.length; i++) {
			int t = arrays[i];
			for (int j = i; j > 0 && arrays[j - 1] > t; j--) {
				arrays[j] = arrays[j - 1];
				arrays[j - 1] = t;				
			}
			
		}
	}
	
	public static void printInts(int[] list){
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
		System.out.println();
	}
}
