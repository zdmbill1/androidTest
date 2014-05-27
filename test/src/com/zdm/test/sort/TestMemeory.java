package com.zdm.test.sort;

import java.util.BitSet;

class LotsOfBooleans {
	boolean a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, aa, ab, ac, ad, ae, af;
	boolean b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, ba, bb, bc, bd, be, bf;
	boolean c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, ca, cb, cc, cd, ce, cf;
	boolean d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, da, db, dc, dd, de, df;
//	boolean e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, ea, eb, ec, ed, ee, ef;
}

class LotsOfInts {
	int a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, aa, ab, ac, ad, ae, af;
	int b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, ba, bb, bc, bd, be, bf;
	int c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, ca, cb, cc, cd, ce, cf;
	int d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, da, db, dc, dd, de, df;
//	int e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, ea, eb, ec, ed, ee, ef;
}

/**
 * @author bill
 *BitSet以64的倍数来申请，即每次申请64，new BitSet(128),new BitSet(65)空间一致
 *除开64以下，64以上的时候每次增加8字节，即按位存储
 */
class LotsOfBitSet {
	BitSet bs = new BitSet(64*2);	
}

public class TestMemeory {
	private static final int SIZE = 100000;

	public static void main(String[] args) throws Exception {
		LotsOfBooleans[] first = new LotsOfBooleans[SIZE];
		LotsOfInts[] second = new LotsOfInts[SIZE];
		BitSet[] third=new BitSet[SIZE];

		System.gc();
		long startMem = getMemory();

		for (int i = 0; i < SIZE; i++) {
			first[i] = new LotsOfBooleans();
		}

		System.gc();
		long endMem = getMemory();

		System.out.println("Size for LotsOfBooleans: " + (endMem - startMem));
		System.out.println("Average size: "
				+ ((endMem - startMem) / ((double) SIZE)));

		System.gc();
		startMem = getMemory();
		for (int i = 0; i < SIZE; i++) {
			second[i] = new LotsOfInts();
		}
		System.gc();
		endMem = getMemory();

		System.out.println("Size for LotsOfInts: " + (endMem - startMem));
		System.out.println("Average size: "
				+ ((endMem - startMem) / ((double) SIZE)));


		System.gc();
		startMem = getMemory();
		for (int i = 0; i < SIZE; i++) {
			
			third[i] = new BitSet(64*2);
			
		}
		System.gc();
		endMem = getMemory();

		System.out.println("Size for LotsOfBitSet: " + (endMem - startMem));
		System.out.println("Average size: "
				+ ((endMem - startMem) / ((double) SIZE)));
		
		//防止被垃圾收集？
		long total = 0;
		for (int i = 0; i < SIZE; i++) {
			// total += (first[i].a0 ? 1 : 0) + second[i].a0;
			total = total + (first[i].a0 ? 1 : 0) + second[i].a0;
		}
		System.out.println(total);

		Runtime runtime = Runtime.getRuntime();
		System.out.println("totalMemory=" + runtime.totalMemory());
	}

	private static long getMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
}
