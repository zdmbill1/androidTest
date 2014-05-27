package com.zdm.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class RandomTestNew {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int num = (int) Math.pow(10, 5);
		int max = (int) Math.pow(10, 9);

		// for(int i=0;i<num;i++){
		// Math.round(Math.random()*max);
		// }

		String str="123456";
		System.out.println(str.substring(0,3));
		System.out.println(str.substring(3,6));
//		System.out.println(str.substring(3,7));
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

		int[] is=new int[num];

//		StringBuffer s = new StringBuffer();
//		for (int i = 0; i < max; i++) {
//			s.append("0");
//		}
//		String ss=s.toString();
		long sum=0;
		
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
		System.out.println((double)(ed.getTime() - sd.getTime())/1000 + "s");
		System.out.println(ed.getTime() - sd.getTime() + "ms");
		System.out.println("随机数生成完毕");
		Iterator<Integer> it=m.keySet().iterator();
		int j=0;
		while (it.hasNext()) {
			sum=sum+(long)Math.pow(2, (Integer) it.next()-1);
//			is[j] = (Integer) it.next();
//			ss=ss.substring(0,(Integer) it.next());
//			DirectSort.sortDirect(is,j);
//			j++;			
		}
		

		System.out.println(new Date().getTime()-ed.getTime()+"ms");
		System.out.println(sum);

	}

}
