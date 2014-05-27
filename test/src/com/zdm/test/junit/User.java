package com.zdm.test.junit;

public class User {

	private String id;
	private String name;
	private String pwd;
	
	public User() {
		super();
	}

	public User(String id, String name, String pwd) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
	}

	public String hello(){
		return "hello";
	}
	
	public boolean bFasle(){
		return false;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + "]";
	}
	
	
}
