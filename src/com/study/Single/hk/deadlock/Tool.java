package com.study.Single.hk.deadlock;

public class Tool {
	private final String name;
	
	public Tool(String name){
		this.name =name;
	}
	public String toString(){
		return "[ "+name+" ]";
	}
}
