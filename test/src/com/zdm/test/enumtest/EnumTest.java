/**
 * 
 */
package com.zdm.test.enumtest;

/**
 * @author bill
 *
 */
public enum EnumTest {

	China("Chinese"),
	England("English"),
	USA("EngLish");

	private String language;
	
	EnumTest(String language) {	
		this.language=language;
	}
	
	public String getLanguage() {
		return language;
	}



	public static void main(String[] args) {
		for(EnumTest et:EnumTest.values()){
			System.out.println("name="+et.name()+" toString="+et.toString()+" language="+et.getLanguage());
			System.out.println(EnumTest.valueOf("China"));
		}
	}
	
}
