package com.study.immutable;

/**
 * 		类为final
 * 		字段类型为 private final
 * 		不提供set方法[必要]
 * */
public final class Person {

	private final String name;
	private final String address;
	
	public Person(String name,String address){
		this.name =name;
		this.address =address;
	}
	public String gerName(){
		return name;
	}
	public String gerAddress(){
		return address;
	}
	public String toString(){
		return "[Person: name= "+name+", address = "+address+"]";
	}
	
}
