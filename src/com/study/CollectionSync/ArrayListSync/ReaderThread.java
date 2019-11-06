package com.study.CollectionSync.ArrayListSync;

import java.util.List;

public class ReaderThread extends Thread{
	
	private final List<Integer> list;
	
	public ReaderThread(List<Integer> list){
		super("ReaderThread");
		this.list =list;
	}
	
	public void run(){
		while(true){
//			for(int n:list){
//				System.out.println(n);
//			}
			synchronized(this){
				for(int n:list){
					System.out.println(n);
				}
			}
		}
	}
}
