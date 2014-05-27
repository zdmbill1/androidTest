package com.zdm.test.sort;

public class QuickSort {

	public static void main(String[] args) {
		int[] list = { 21, 63, 45, 8, 7, 9, 10, 256, 248, 14, 99, 134, 555, 3,87, 69 };
		QuickSort qs = new QuickSort();
		qs.quick(list);
//		qs.swap(list,1,2);
		
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
		System.out.println();
	}

	public int getMiddle(int[] list, int low, int high) {
		int tmp = list[low]; // 数组的第一个作为中轴
//		while (low < high) {
//			while (low < high && list[high] > tmp) {
//				high--;
//			}
//			list[low] = list[high]; // 比中轴小的记录移到低端
//			while (low < high && list[low] < tmp) {
//				low++;
//			}
//			list[high] = list[low]; // 比中轴大的记录移到高端
//		}
//		list[low] = tmp; // 中轴记录到尾
		
		while(low<high){
			low=low+1;
			if(list[low]<tmp){
				swap(list,low,low-1);
			}else if(list[low]>tmp){
				swap(list,low,high);
				low=low-1;				
				high=high-1;
				
			}
		}
		return low; // 返回中轴的位置
	}
	
	public void swap(int[] list,int a,int b){
		int tmp=list[a];
		list[a]=list[b];
		list[b]=tmp;
	}

	public void _quickSort(int[] list, int low, int high) {
		if (low < high) {
			int middle = getMiddle(list, low, high); // 将list数组进行一分为二
			_quickSort(list, low, middle - 1); // 对低字表进行递归排序
			_quickSort(list, middle + 1, high); // 对高字表进行递归排序
		}
	}

	public void quick(int[] str) {
		if (str.length > 0) { // 查看数组是否为空
			_quickSort(str, 0, str.length - 1);
		}
	}
}
