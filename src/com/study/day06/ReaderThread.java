package com.study.day06;

public class ReaderThread extends Thread{
	private final Data data;
	public ReaderThread(Data data){
		this.data=data;
	}
	public void run(){
		while(true){
			char[] readbuf =data.read();
			System.out.println(Thread.currentThread().getName()+" reads "+String.valueOf(readbuf));
		}
	}
}
