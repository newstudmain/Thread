package com.study.ThreadGroup;

import java.util.LinkedList;

/*
 *  在 ThreadPoolSimple 类中定义了一个 LinkedList 类型的 workQueue 成员变量， 
	它表示工作队列， 用来存放线程池要执行的任务， 每个任务都是 Runnable 实例。 
	
	ThreadPoolSimple 类的客户程序（ 利用 ThreadPool 來执行任务的程序） 
	只要调用 ThreadPoolSimple 类的 execute(Runnable task)方法， 就能向线程池提交任务。 
	在 ThreadPoolSimple 类的 execute()方法中， 先判断线程池是否己经关闭。 
	如果线程池已经关闭， 就不再接收任务， 否则就把任务加入到工作队列中， 并且唤醒正在等待任务的工作线程。
	
	在ThreadPoolSimple 类的构造方法中， 会创建并启动若干工作线程， 工作线程的数目由构造方法的参数 Poolsize决定。 
	WorkThread 类表示工作线程， 它是ThreadPoolSimple 类的内部类。 
	
	工作线程从工作队列中取出一个任务， 接着执行该任务， 然后再从工作队列屮取出下一个任务并执行它， 如此反复。
	
	工作线程从工作队列中取任务的操作是由ThreadPoolSimple 类的 getTask()方法实现的：
		它的处理逻辑如下： 
				• 如果队列为空并且线程池已关闭， 那就返回 null, 表示己经没有任务可以执行了；
				• 如果队列为空并且线程池没有关闭， 那就在此等待， 到其他线程将其唤醒或者中断；
				• 如果队列中有任务， 就取出第一个任务并将其返回
	
	线程池的 join()和 closed()方法都可用来关闭线程池。
		join()方法确保在关闭线程池之前， 工作线程把队列中的所有任务都执行完。 
		而  closed()方法则立即清空队列， 并中断所有的工作线程。
		ThreadPoolSimple 类是 ThreadGroup 类的子类。 ThreadGroup类表示线程组， 它提供了一些管理线程组中线程的方法。 
		例如，interrupt()方法相当于调用线程组中所有活着的线程的 interrupt()方法。 
		线程池中的所有工作线程都加入到当前 ThreadPoolSimple对象表示的线程组中。 
	
	
 * */
public class ThreadPoolSimple extends ThreadGroup{
	private boolean isClosed=false;						// 线程池是否关闭
	private LinkedList<Runnable> workQueue;				// 工作队列
	private static int threadPoolID;					// 线程池ID
	private int threadID;								// 工作线程ID
	
	public ThreadPoolSimple(int Poolsize) {				// Poolsize 指定线程池中的工作线程数目
		super("ThreadPool- "+(threadPoolID++));
		setDaemon(true);
		workQueue = new LinkedList<Runnable>();			// 创建工作队列
		
		for(int i=0;i<Poolsize;i++) {
			new WorkThread().start();					// 创建并启动工作线程
		}
	}

	public synchronized void execute(Runnable task) {
		if (isClosed) {
			throw new IllegalStateException();			// 线程池关闭则抛出异常
		}
		if (task!=null) {
			workQueue.add(task);
			notify();
		}
	}
	
	/** 从工作队列中取出 一个任务，工作线程会调用此方法 */
	protected synchronized Runnable getTask()throws InterruptedException {
		while (workQueue.size() == 0) {
			if (isClosed) return null;
			wait();										// 如果工作作队列中没有任务. 就等待任务
		}
		return workQueue.removeFirst();					// removeFirst() 移除并返回此列表的第一个元素。
	}
	
	/** 关闭线程池 */
	public synchronized void close() {
		if (!isClosed) {
			isClosed = true;
			workQueue.clear();
			interrupt();
		}
	}
	
	/** 匁待:1:作线构把所存任务执行完 */
	public void join() {
		synchronized (this) {
			isClosed = true;
			notifyAll();
		}
		
		Thread[] threads = new Thread[activeCount()];
		
		int count =enumerate(threads);					// enumerate()方法继承自 ThreadGroup 类， 获得线程组中当前活着的所有工作线程
		for (int i=0; i<count;i++) { 					// 等待所有工作线程运行结束
			try {
				threads[i].join();						// 等待工作线程运行结束
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 内部类： 工作线程*/
	private class WorkThread extends Thread {
	
		public WorkThread() {
			super(ThreadPoolSimple.this,"threadPool- "+(threadPoolID++));
		}
		//加入到当前的ThreadPoolSimple线积组中
		public void run() {
			while(!isInterrupted()) { 					// isInterrupted()方法继承 自 Thread 类， 判断线程是否被中断
				Runnable task = null;
				try { 
					task = getTask();					// 取出任务
				}catch (InterruptedException e){}
														// 如果 getTaskO返冋 null 或荇线种执行 getTaskO时被中晰 ， 则结束此线程
				if (task == null) return;
				try { 									// 运行任务. 异常在 catch 代码块中捕获
					task.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}																																																																																																																																							
	}
}



