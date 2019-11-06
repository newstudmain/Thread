package com.study.day01;

public class Bank{
	private static int money;
	private String name;
	Object obj = new Object();
	
	//存款
	public synchronized void deposit(int m) throws InterruptedException{
		obj.wait();
		money=money+m;
	}
	//取款
	public synchronized boolean withdrew(int m){
		obj.notify();
		if(money>m){
			money=money-m;
			return true;//取款成功
		}else{
			return false;//余额不足
		}
	}
	//取款
	public boolean withdrew_e(int m){
		synchronized(this){
			if(money>m){
				money=money-m;
				return true;//取款成功
			}else{
				return false;//余额不足
			}
		}
	}
	
	//取款
	public static synchronized boolean withdrew_t(int m){
		if(money>m){
			money=money-m;
			return true;//取款成功
		}else{
			return false;//余额不足
		}
	}
	//取款
	public static boolean withdrew_te(int m){
		synchronized(Bank.class){
			if(money>m){
				money=money-m;
				return true;//取款成功
			}else{
				return false;//余额不足
			}
		}
	}
	
	public Bank(int money,String name){
		this.money=money;
		this.name=name;
	}
	public String getName(){
		return name;
	}
}
