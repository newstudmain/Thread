package com.study.Single;

/*
 	SharedResource 共享资源类
 		-能被多个线程访问，包含许多方法
 		-需要处理线程安全问题 synchronized
 */

public class Gate {
	private String name;
	private String address;
	private int counter=0;
	
	public synchronized void pass(String name,String address){
		counter=counter+1;
		this.name=name;
		this.address=address;
		check();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public synchronized String toString(){
		return "No. "+counter+": "+name+", "+address;
	}
	
	private void check(){
		if(name.charAt(0)!=address.charAt(0)){
			System.out.println("******BROKEN******"+ toString());
		}
	}
	
//	public synchronized void setName(String name){
//		this.name=name;
//	}
//	public synchronized void setAddress(String address){
//		this.address=address;
//		counter=counter+1;
//		check();
//	}
}
