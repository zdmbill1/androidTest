package com.zdm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		BufferedReader in = null;
		
		in = new BufferedReader(new FileReader(new File("/home/bill/dao.zhangaqVue_view/cc_vob_storage/aqMDS/aqVUE/ui/shell/src/main/webapp/upload/t.csv")));

		String tmpStr=null;
		while((tmpStr=in.readLine())!=null){
			System.out.println("tmpStr===="+new String(tmpStr.getBytes(),"UTF-8"));
			System.out.println("tmpStr----"+tmpStr);
		}

		String tmp="abb.csvff";
		String t[]=tmp.split("\\.");
		System.out.println(tmp.split("\\.").length);
	}

}
