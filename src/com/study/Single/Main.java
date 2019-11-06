package com.study.Single;

public class Main {
	public static void main(String[] args){
		System.out.println("Tesing Gate, hit CTRL+C to exit.");
		Gate gate = new Gate();
//		Gate gate2 = new Gate();
//		Gate gate3 = new Gate();
		
		new UserThread01(gate,"Alice","Alaska").start();
		new UserThread01(gate,"Bobby","Brazil").start();
		new UserThread01(gate,"Chris","Canada").start();
		
//		new UserThread(gate,"Alice_e","Alaska_e").start();
//		new UserThread(gate2,"Bobby_e2","Brazil_e2").start();
//		new UserThread(gate3,"Chris_e3","Canada_e3").start();
	}
}
