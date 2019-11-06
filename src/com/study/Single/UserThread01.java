package com.study.Single;

public class UserThread01 extends Thread{
	private final Gate gate;
	private final String myname;
	private final String myaddress;
	
	public UserThread01(Gate gate,String myname,String myaddress){
		this.gate=gate;
		this.myname=myname;
		this.myaddress=myaddress;
	}
	
	public void run(){
		System.out.println(myname + ": "+"BEGIN");
		while(true){
			gate.pass(myname,myaddress);
//			gate.setName(myname);
//			gate.setAddress(myaddress);
		}
	}
}
