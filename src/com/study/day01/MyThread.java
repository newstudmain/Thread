package com.study.day01;

public class MyThread extends Thread{
	public void run(){
		for(int i=0;i<1000;i++){
			System.out.print("good!");
		}
	}
}
