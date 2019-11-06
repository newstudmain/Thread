package com.study.day06;

public class Main {
	public static void main(String[] args){
		Data data =new Data(10);
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new WriterThread(data,"ABVCHJKHJLHJGUIGUIG").start();
		new WriterThread(data,"hdjhfkjkjkjkdsihdhf").start();
	}
}
