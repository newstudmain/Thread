package com.study.ThreadGroup;

public class ThreadPoolSimpleTest {
	private final int POOL_SIZE	 =4;
	private int taskNum =30;
	private ThreadPoolSimple threadPool;
	
	public ThreadPoolSimpleTest() {
		threadPool = new ThreadPoolSimple(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		System.out.println("线程池已创建...");
	}
	
	public static void main(String[] args) {
		new ThreadPoolSimpleTest().service();
	}
	
	public void service() {
		for(int i=0;i<taskNum;i++) {
			threadPool.execute(createTask(i));
			//threadPool.join();
		}
	}
	
	public static Runnable createTask(final int taskID) {
		return new Runnable() {
			@Override
			public void run() {
				System.out.println("Task "+taskID+" :start");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println("Task "+taskID+" :end");
				}
			}
		};
	}
}
