package com.study.Single.hk.deadlock;

public class Main {
	public static void main(String[] args){
		System.out.println("testing EaterThread, hit CTRL+C exit.");
		Tool Spoon =new Tool("Spoon");
		Tool Fork =new Tool("Fork");
		new EaterThread("Alice",Spoon,Fork).start();
		new EaterThread("Bobby",Fork,Spoon).start();
	}
}
