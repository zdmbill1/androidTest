package com.zdm.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class Test {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(new Date());
		Random r=new Random();
		for(int i=0;i<10;i++){
			System.out.println(r.nextInt(5));
		}
		
		System.out.println(new Date());
		

		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		String processID = processName.substring(0,processName.indexOf('@'));
		System.out.println("processID="+processName);
		
		User u=new User("zdm", 1);
		testPerUser(u);
		Date sd=new Date();
//		System.out.println(addTest(41));
		Date ed=new Date();
		System.out.println("耗时="+(ed.getTime()-sd.getTime())+"ms");
		
		List<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<10;i++){
			l.add(i);
		}
		Iterator<Integer> itr=l.iterator();
		int j=0;
		while (itr.hasNext()) {
			j=j+1;
			itr.next();
			if(j%2==0){
				itr.remove();
			}			
		}
		
		System.out.println(l.size());
		
		String strtmp="  aaa cd22a   13";
		System.out.println(strtmp.replaceAll(" ", "&nbsp;"));
		
		System.out.println(23%5);
		
		System.out.println(Integer.toBinaryString(120));
		System.out.println(120>>>2);
		System.out.println(Integer.toBinaryString(120>>>2));
		System.out.println(Integer.toBinaryString(120>>2));
		int i=55;
		System.out.println(i--+" "+i);
		i=55;
		System.out.println(--i+" "+i);
		
		if(i!=55&&i!=56){
			System.out.println("============"+i);
		}
		
		try {
			InetAddress address=InetAddress.getByName("172.17.64.255");
			if(address instanceof Inet4Address){
				System.out.println(address.getHostAddress()+" is ipv4");
			}else if(address instanceof Inet6Address){
				System.out.println(address.getHostAddress()+" is ipv6");
			}else{
				System.out.println(address.getHostAddress() +" is not correct.");
			}
			
			if(address.isReachable(5000)){
				System.out.println("Success");
			}else{
				System.out.println("Fail");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("java");
		list.add("aaa");
		list.add("java");
		list.add("java");
		list.add("bbb");
		System.out.println(list.size());
//		list.removeAll(new String[]{"",""});
		System.out.println("del="+list.size());
		
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("a", 2);
		m.put("b", "aa");
		m.put("c", 2.34);
		
		Iterator<Object> it=m.values().iterator();
		while(it.hasNext()){
			System.out.println(it.toString()+"==="+it.next());
		}
		
		Set<String> key=m.keySet();
		String keystr="";
		for(Iterator<String> iter=key.iterator();iter.hasNext();){
			keystr=(String) iter.next(); 
			System.out.println(keystr+"==="+m.get(keystr));
		}
		
		Set<Entry<String, Object>> enkey=m.entrySet();
		for(Iterator<Entry<String, Object>> iter2=enkey.iterator();iter2.hasNext();){
			Map.Entry<String, Object> ent=(Entry<String, Object>) iter2.next();
			System.out.println(ent.getKey()+"--->"+ent.getValue());
		}
		
		String rstr="次>";
		if(rstr.indexOf(" >")==-1){
		rstr=rstr.replace(">", " >");
		}
		System.out.println(rstr);
		
		switch (2) {
		case 1:
			System.out.println("1");
			break;
		case 2:
			
		case 3:
			System.out.println("3");
			break;
		default:
			System.out.println("default");
			break;
		}
		
		Object tmp = null;
		System.out.println((Boolean)tmp);
		
		System.out.println(digui(10));
		
		double sum=0;
		for(i=12;i<=30;i++){
			sum=sum+Math.pow(2, i);
		}
		System.out.println(sum);
		
		String test="I am 淘宝?";
		System.out.println(test.toCharArray().length);
		
		System.out.println(test.toCharArray()[6]);
		
		int x=test.toCharArray()[5];
		System.out.println(x);
		System.out.println((char) 20013);
		
		System.out.println(ClassLoader.getSystemResource(""));
		
		System.out.println(new Date().getTime());
		System.out.println(new Date(1249004760174l));
		
		
		switch (1)

		{

		case 1:

		case 2:
		case 3:
			System.out.println("haha");
			break;
		case 4:
			System.out.println("hehe");
		default:
			System.out.println("default");

		}
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		
		String str="Duplicate entry '123' for key 'service_request_id'";
		System.out.println(str.split("'")[1]);
		
		System.out.println(System.currentTimeMillis());
	}
	
	public void t(){
		getClass().getClassLoader().getResource("").toString();
	}
	
	public static void testPerUser(Person p){
		System.out.println(p.getName());
	}
	
	public static long addTest(int n){
		if(n<=1){
			return 1;
		}else{
			return addTest(n-1)+addTest(n-2);
		}
	}
	
	public static int digui(int n){
		if(n>1){
			return digui(n-1);
		}else{
			return 1;
		}
	}
}
