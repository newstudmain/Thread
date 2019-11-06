package com.study.immutable;

public class PrintPersonThread extends Thread{
	private Person person;
	public PrintPersonThread(Person person){
		this.person =person;
	}
	public void run(){
		while(true){
			System.out.println
			(Thread.currentThread().getName()+"prints"+person);
		}
	}
}

/**
 * Thread.currentThread().getName()+"prints"+person
 * 
 *  Thread.currentThread() 静态方法
 *  	获取当前调用currentThread()方法的线程
 *  getName() 实例方法
 *  	获取线程名称
 *  +person / person.toStirng()
 *  	当通过+连接时，自动调用toStirng()方法
 * */
