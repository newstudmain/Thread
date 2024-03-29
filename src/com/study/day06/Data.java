package com.study.day06;

public class Data {
	private final char[] buffer;
	private final ReadWriteLock lock =new ReadWriteLock();
	
	public Data(int size){
		buffer =new char[size];
		for(int i=0;i<buffer.length;i++){
			buffer[i]='*';
		}
	}
	public char[] read(){
		lock.readLock();
		try{
			return doRead();
		}finally{
			lock.readUnlock();
		}
	}
	public void write(char c){
		lock.writeLock();
		try{
			doWrite(c);
		}finally{
			lock.writeUnlock();
		}
	}
	public void slowly(){
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public char[] doRead(){
		char[] newbuf =new char[buffer.length];
		for(int i=0;i<buffer.length;i++){
			newbuf[i]=buffer[i];
		}
		slowly();
		return newbuf;
	}
	public void doWrite(char c){
		for(int i=0;i<buffer.length;i++){
			buffer[i]=c;
			slowly();
		}
	}
}