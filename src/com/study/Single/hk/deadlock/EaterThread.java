package com.study.Single.hk.deadlock;

public class EaterThread extends Thread{
	private String name;
	private Tool lefthand;
	private Tool righthand;
	
	public EaterThread(String name,Tool lefthand,Tool righthand){
		this.name=name;
		this.lefthand=lefthand;
		this.righthand=righthand;
	}
	public void eat(){
		synchronized(lefthand){
			System.out.println(name+" takes up "+lefthand+"(left).");
			synchronized(righthand){
				System.out.println(name+" takes up "+righthand+"(right).");
				System.out.println(name+" is eating now yum yum!");
				System.out.println(name+" puts down "+righthand+"(right).");
			}
			System.out.println(name+" puts down "+lefthand+"(left).");
		}
	}
	public void run(){
		while(true){
			eat();
		}
	}
}
