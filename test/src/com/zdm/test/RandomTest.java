package com.zdm.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.zdm.test.sort.BitSort;
import com.zdm.test.sort.InsertSort;
import com.zdm.test.sort.QuickSort;

/**
 * @author bill 结论：快速排序和位图排序较快，位图排序和max影响较大，num和max比较接近(三个数量级内)时推荐位图排序
 *         快速排序和max基本无影响，当num变大时，速度变慢明显
 */
public class RandomTest {

	final static int num = (int) Math.pow(10, 7);
	final static int max = (int) Math.pow(10, 8);

	static int[] randoms=new int[num];
	
	/**
	 * @param args
	 *            生成一百万个不重复的int<一亿
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 1; i++) {
			// new Thread(new RandomTest().new MyThread("zdm"+i)).start();
		}
		// createRandomNums();
		readRandomNums();
		
		Date sd = new Date();
//		InsertSort.insertSort(randoms);
//		BitSort.bitSetSort(randoms, max);
		BitSort.bitsort(randoms, max);
		System.out.println("排序消耗:" + (new Date().getTime() - sd.getTime())
				+ "ms");

		
		// int []test = {2,6,5,8,7,9,10,256,248,14};
		// sortDirect(test);
		// for(int i=0; i<test.length; i++){
		// System.out.print(test[i]+",");
		// }
	}

	public static void readRandomNums() throws IOException {
		File f = new File("/home/bill/randomNums.txt");
		if (!f.exists()) {
			System.out.println("not found randomNums file");
			return;
		}
		Date sd = new Date();
		// 按行读OutOfMemoryError
		// FileReader fr=new FileReader(f);
		// BufferedReader br=new BufferedReader(fr);
		// String tmp=br.readLine();
		// StringBuffer sb=new StringBuffer();
		//
		// while(tmp!=null){
		// sb.append(tmp);
		// System.out.println(tmp);
		// tmp=br.readLine();
		//
		// }
		// String[] is=sb.toString().split(",");

		Reader reader = null;
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(f));
			int tempchar;
			StringBuffer sb=new StringBuffer();
			int i = 0;
			int n=0;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					// 44=,
					if (tempchar == 44) {
						randoms[n]=Integer.parseInt(sb.toString());
						n++;
						sb.delete(0, i);
						i = 0;
					} else {
						sb.append((char) tempchar);
						i++;
					}					
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("读取随机数消耗时间:");
		System.out.println((double) (new Date().getTime() - sd.getTime())
				/ 1000 + "s");
		System.out.println(new Date().getTime() - sd.getTime() + "ms");
	}

	public static void createRandomNums() throws IOException {

		// int num = (int) Math.pow(10, 7);
		// int max = (int) Math.pow(10, 9);
		System.out.println("开始生成随机数!");
		// for(int i=0;i<num;i++){
		// Math.round(Math.random()*max);
		// }

		// System.out.println(str.substring(3,7));
		Date sd = new Date();
		// System.out.println(ed.getTime()-sd.getTime()+"ms");
		// Random()比Math.random效率高2倍多---猜测主要是Math方法还要经过其他操作
		Random r = new Random();
		// 保存在list中然后比较是否存在效率太低，半小时无结果
		/*
		 * List<Integer> l = new ArrayList<Integer>(); for (int i = 0; i < num;
		 * i++) { int tmp = r.nextInt(max); if (!l.contains(tmp)) { l.add(tmp);
		 * } }
		 */

		int[] is = new int[num];

		// StringBuffer s = new StringBuffer();
		// for (int i = 0; i < num; i++) {
		// s.append("0");
		// }
		// String ss=s.toString();

		/*
		 * 用StringBuffer消耗内存太大，抗不住
		 */
		File f = new File("/home/bill/randomNums.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
		// 使用BufferedWriter提高速度10%左右
		BufferedWriter bw = new BufferedWriter(fw);
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (int i = 0; i < num; i++) {
			Integer tmp = r.nextInt(max);
			while (m.get(tmp) != null) {
				tmp = r.nextInt(max);
			}
			m.put(tmp, 1);
			bw.write(tmp + ",");
			// fw.write(tmp + ",");
		}

		bw.flush();
		bw.close();
		Date ed = new Date();
		System.out.println("生成随机数消耗时间:");
		System.out.println((double) (new Date().getTime() - sd.getTime())
				/ 1000 + "s");
		System.out.println(ed.getTime() - sd.getTime() + "ms");
		System.out.println("随机数生成完毕");
		// fw.close();
	}

	public static void randomT() {

		// for(int i=0;i<num;i++){
		// Math.round(Math.random()*max);
		// }

		// System.out.println(str.substring(3,7));
		Date sd = new Date();
		// System.out.println(ed.getTime()-sd.getTime()+"ms");
		// Random()比Math.random效率高2倍多---猜测主要是Math方法还要经过其他操作
		Random r = new Random();
		// 保存在list中然后比较是否存在效率太低，半小时无结果
		/*
		 * List<Integer> l = new ArrayList<Integer>(); for (int i = 0; i < num;
		 * i++) { int tmp = r.nextInt(max); if (!l.contains(tmp)) { l.add(tmp);
		 * } }
		 */

		int[] is = new int[num];

		// StringBuffer s = new StringBuffer();
		// for (int i = 0; i < num; i++) {
		// s.append("0");
		// }
		// String ss=s.toString();

		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (int i = 0; i < num; i++) {
			Integer tmp = r.nextInt(max);
			while (m.get(tmp) != null) {
				tmp = r.nextInt(max);
			}
			m.put(tmp, 1);
		}

		Date ed = new Date();
		System.out.println("生成随机数消耗时间:");
		System.out.println((double) (ed.getTime() - sd.getTime()) / 1000 + "s");
		System.out.println(ed.getTime() - sd.getTime() + "ms");
		System.out.println("随机数生成完毕");
		System.out.println("遍历map开始");
		Date tmpd = new Date();
		Iterator<Integer> it = m.keySet().iterator();
		int j = 0;
		while (it.hasNext()) {
			is[j] = (Integer) it.next();

			j++;
		}
		System.out.println("遍历map耗时:" + (new Date().getTime() - tmpd.getTime())
				+ "ms");
		ed = new Date();
		QuickSort qs = new QuickSort();
		// 一百万大概185ms,比较稳定,和max无关
		// qs.quick(is);
		// 十万数据大概8000ms
		// DirectSort.sortDirect(is, is.length);
		// 十万数据5000-12000ms不稳定
		// DirectSort.insertSort(is);
		// 第三快，大概300ms
		// BitSort.bitsort(is,max);
		// 第二快,大概150-180ms，和max有关
		BitSort.bitSetSort(is, max);

		System.out.println("排序消耗:" + (new Date().getTime() - ed.getTime())
				+ "ms");

		/*
		 * for(int i=0;i<is.length;i++){ System.out.print(is[i]+",");
		 * if((i+1)%100==0){ System.out.print("\n"); } }
		 */

	}

	public static void sortDirect(int[] nums, int maxLen) {
		// int len=nums.length-1;
		maxLen = maxLen - 1;
		int temp, j = 0;
		for (int i = 0; i < maxLen; i++) {
			temp = nums[i + 1];
			if (nums[i] > temp) {
				for (j = i; j < maxLen && nums[j] > temp; j++) {
					temp = nums[j + 1];
					nums[j + 1] = nums[j];
					nums[j] = temp;
				}
			}
		}
	}

	class MyThread implements Runnable {

		private String name;

		public MyThread(String name) {
			this.name = name;
		}

		public MyThread() {
			super();
			// TODO Auto-generated constructor stub
		}

		public void run() {
			System.out.println(name);
			RandomTest.randomT();
		}

	}

}
