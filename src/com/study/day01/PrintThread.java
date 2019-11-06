package com.study.day01;

public class PrintThread extends Thread{
	private String massage;
	public PrintThread(String massage){
		this.massage=massage;
	}
	public void run(){
		for(int i=0;i<1000;i++){
			System.out.println(massage);
		}
	}
}
