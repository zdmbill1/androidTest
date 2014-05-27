package com.zdm.test;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {

	private String name;
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String toString() {
		System.out.println(super.toString());
		System.out.println(ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE));
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Person p = new Person("test", 1);
		Person p1 = new Person("test", 1);
		System.out.println(p.hashCode());
		System.out.println(p1.hashCode());
		System.out.println(new HashCodeBuilder().append("abc1").toHashCode());
		System.out.println(p.toString());

	}

}
