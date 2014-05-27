package com.zdm.test.sort;

import java.util.BitSet;

public class BitSort {

	static int WORDLENGTH = 32;
	static int SHIFT = 5;
	static int MASK = 0x1F;
	static int MAX = 10000000;
	static int[] A = null;

	/**
	 * 位图排序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int[] data = { 568746, 12354, 45798, 1245, 8, 17, 1, 5 };
		bitsort(data, MAX);
		// bitSetSort(data,MAX);
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + " ");
		}
		System.out.println();
		
		int a=-1;
		System.out.println(Integer.toBinaryString(a));
		System.out.println(Integer.toBinaryString(a).length());
		System.out.println(Integer.toBinaryString(a>>1));
		System.out.println(a>>1);
		
	}

	/**
	 * @param array
	 * @param max
	 *            简单明了效率高 
	 *            BitSet以64的倍数来申请，即每次申请64，new BitSet(128),new
	 *            BitSet(65)空间一致 除开64以下，64以上的时候每次增加8字节，即按位存储
	 */
	public static void bitSetSort(int[] array, int max) {
		BitSet bitSet = new BitSet(max);
		for (int i : array) {
			bitSet.set(i);
		}
		int j = 0;
		for (int i = 0; i < max; i++) {
			if (bitSet.get(i)) {
				array[j] = i;
				j = j + 1;
			}
		}
	}

	/**
	 * @param array
	 * @param max
	 * 严格按位操作，1个int=4byte=32bit
	 */
	public static void bitsort(int[] array, int max) {
//		MAX = max;
		A = new int[(1 + max / WORDLENGTH)];
		for (int i = 0; i < array.length; i++)
			set(array[i]);
		int j = 0;
		for (int i = 0; i < max; i++) {
			if (test(i)) {
				array[j] = i;
				j = j + 1;
			}
		}
	}

	// 将A[i>>SHIFT]的第(i & MASK)位置1
	//i>>SHIFT等效于i/(2^SHIFT),这里就是除32取整
	//i & MASK等效2^SHIFT-1取余
	public static void set(int i) {
		A[i >> SHIFT] =A[i >> SHIFT]| (1 << (i & MASK));
	}

	// 测试A[i>>SHIFT]的第(i & MASK)位置是否为1
	public static boolean test(int i) {
		return (A[i >> SHIFT] & (1 << (i & MASK))) == (1 << (i & MASK));
	}

}
