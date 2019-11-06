package com.study.day01;

public class Printer implements Runnable{
	private String massage;
	public Printer(String massage){
		this.massage=massage;
	}
	public void run(){
		for(int i=0;i<1000;i++){
			System.out.println(massage);
		}	
	}
}
