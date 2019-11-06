package com.stud.basic;

/*
 * 大多数停止一个线程的操作使用 Thread.interrupt() 方法， 
 * 尽管方法的名称是 停止， 中止 的意思， 但这个方法不会终止一个正在运行的线程， 
 * 还需要加入一个判断才可以完成线程的停止。 
 * 
 * Java 中有以下 3 种方法可以终止正在运行的线程：
		1. 使用退出标志， 使线程正常退出， 也就是当 run 方法完成后线程终止。
		2. 使用 stop 方法强行终止线程， 但是不推荐使用这个方法， 因为 stop 和 suspend 及resume
		        一样， 都是作废过期的方法， 使用它们可能产生不可预料的结果。
		3. 使用 interrupt 方法中断线程。

 * 
 * 	调 用 interrupt()方 法 来 停 止 线 程， 但 方 法 的 使 用 效 果 并 不 像 for+break
	语 句 那 样， 马 上 就 停 止 循 环。 调 用 方 法 仅 仅 是 在 当 前 线 程 中 打 了 一 个 停
	止的标记， 并不是真的停止线程。
 * 
 * 异常法 停止线程 throw new InterruptedException()
 * 
 * 				if (this.interrupted()) {
					System.out.println("已经是停止状态了!我要退出了!");
					throw new InterruptedException();
				}

 * 在sleep() 时停止
 * 
 * 
 * */
public class Interrupt {
	
	public static void main(String[] args) {
		MyThreadStop2 thread = new MyThreadStop2();//MyThreadStop2/MyThreadStop()
		thread.start();
        try {
			Thread.sleep(0);
/*          调 用 interrupt()方 法 来 停 止 线 程， 但 方 法 的 使 用 效 果 并 不 像 for+break
			语 句 那 样， 马 上 就 停 止 循 环。 调 用 方 法 仅 仅 是 在 当 前 线 程 中 打 了 一 个 停
			止的标记， 并不是真的停止线程。										*/
			//thread.sleep(0);
			thread.interrupt();
			//Thread.sleep(100);
			System.out.println("是否已经停止 1？=" + thread.isInterrupted());//isInterrupted
			System.out.println("是否已经停止 2？=" + thread.isInterrupted());//isInterrupted
			//System.out.println("是否已经停止 3？=" + thread.interrupted());
			System.out.println("end!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("catch...InterruptedException");
			//e.printStackTrace();
		}
	}
}

class MyThread extends Thread {
	public void run() {
		super.run();
		for(int i =0;i<50000;i++){					// ? 当i<500000 时 main 方法中的 输出语句 不显示 ，待查！
			System.out.println("i= "+(i+1));
		}
	}
}

class MyThreadStop extends Thread {
	public void run() {
		super.run();
		for (int i = 0; i < 50; i++) {
			try {
				this.sleep(1);
				if (this.interrupted()) {
					System.out.println("退出前"+this.isInterrupted());
					System.out.println("已经是停止状态了!我要退出了!");
					break;
				}
			} catch (InterruptedException e) {
				System.out.println("catch (InterruptedException e)...");
			}
		}
		System.out.println("我被输出，如果此代码是for又继续运行，线程并未停止！"); //虽然停止了线程，但利用 break跳出循环， 但如果 for语句下面还有语句， 还是会继续运行的。 
	}
}

class MyThreadStop2 extends Thread {
	@Override
	public void run() {
		super.run();
		try {
			for (int i = 0; i < 500000; i++) {
				if (this.isInterrupted()) {
					System.out.println("this.isInterrupted() "+this.isInterrupted());
					System.out.println("已经是停止状态了!我要退出了!");
					throw new InterruptedException();
				}
				System.out.println("i=" + (i + 1));
			}
			System.out.println("我在for下面");
		} catch (InterruptedException e) {
			System.out.println("进MyThread.java类run方法中的catch了！");
			e.printStackTrace();
		}
	}
}



/*
 * 判断线程是否是停止状态
	在介绍如何停止线程的知识点前， 先来看一下如何判断线程的状态是不是停止的。 
		1 ) this.interrupted(): 测试当前线程是否已经是中断状态，执行后具有将状态标志置清除为 false 的功能。
		2 ) this.islnterrupted(): 测试线程 Thread 对象是否已经是中断状态， 但不清除状态标志。
		
	在java的 SDK中， Thread.java类里提供了两种方法。
		1. this.interrupted(): 测试当前线程是否已经中断。
				测试当前线程是否已经中断， 当前线程是指运行this.interrupted() 方法的线程。 
					System.out.println(" 是否停止1 ? ="+thread.interrupted());
					System.out.println(" 是否停止2 ? ="+thread.interrupted());
					
					>>i= 50000
					>>是否停止1？=false
					>>是否停止2？=false
					
					对象上调用代码:thread.interrupt()来停止对象所代表的线程，在后面又使用代码:thread.interrupted()
					来判断 thread 对象所代表的线程是否停止， 但从控制台打印的结果来看， 线程并未停止 ， 
					这也就证明了 interrupted() 方法的解释： 测试当前线程是否已经中断。 
					这个 当前线程是 main， 它从未中断过， 所以打印的结果是两个 false。
					
				如何使 main 线程产生中断效果呢？
					Thread.currentThread().interrupt();
					System.out.println("是否停止1？="+thread.interrupted());
					System.out.println("是否停止2？="+thread.interrupted());
					
					>>i= 50000
					>>是否停止1？=true
					>>是否停止2？=false
					
					从上述的结果来看， 方法 interrupted() 的确判断岀当前线程是否是停止状态。 
					但为什么第2个布尔值是 false 呢？ 官方帮助文档中对 interrupted 方法的解释：
						测试当前线程是否已经中断。 线程的中断状态由该方法清除。 
						换句话说， 如果连续两次调用该方法， 则第二次调用将返回 false 
						在第一次调用已清除了其中断状态之后， 且第二次调用检验完中断状态前， 当前线程再次中断的情况除外）。
						interrupted() 方法具有清除状态的功 能，所以第 2次调用interrupted() 方法返回的值是 false。


		2. this.isInterrupted(): 测试线程是否已经中断
				isInterruptedO 方法， 声明如下：
				public boolean islnterrupted()
				从声明中可以看出isInterrupted() 方法不是static的。
				
		            Thread.sleep(2000); // 当main线程sleep 2秒时，MyThread 线程已经执行完毕终止，
		            thread.interrupt();
		            System.out.println("是否已经停止 1？=" + thread.isInterrupted());
		            System.out.println("是否已经停止 2？=" + thread.isInterrupted());
				
					>>i= 50000
					>>是否停止1？=false
					>>是否停止2？=false
					
					-----------------------？？？？？？？？？？
					Thread.sleep(0);	// 当main线程sleep 0秒时，如果MyThreadStop 线程已经执行完毕终止，此时线程Interrupted 状态将被重置
		            thread.interrupt();
		            
					>>是否已经停止 1？=true
					>>已经是停止状态了!我要退出了!
					>>我被输出，如果此代码是for又继续运行，线程并未停止！
					>>是否已经停止 2？=false
					>>end!
					
					------------------------
					
					//Thread.sleep(0);
		            thread.interrupt();
		            System.out.println("是否已经停止 1？=" + thread.isInterrupted());
		            System.out.println("是否已经停止 2？=" + thread.isInterrupted());
					
					>>是否已经停止 1？=true
					>>i= 1
					>>i= 2
					>>i= 3
					>>i= 4
					>>是否已经停止 2？=true
					
					
					为什么Thread.isInterrupted()总是返回false？
						要中断一个Java线程，可调用线程类（Thread）对象的实例方法：interrupte()；
						然而interrupte()方法并不会立即执行中断操作；
						具体而言，这个方法只会给线程设置一个为true的中断标志（中断标志只是一个布尔类型的变量），
						而设置之后，则根据线程当前的状态进行不同的后续操作。如果，线程的当前状态处于非阻塞状态，
						那么仅仅是线程的中断标志被修改为true而已；如果线程的当前状态处于阻塞状态，
						那么在将中断标志设置为true后，如果是wait、sleep以及jion三个方法引起的阻塞，
						那么会将线程的中断标志重新设置为false，并抛出一个InterruptedException。
					
					InterruptedException是什么？
						意思是说当一个线程处于等待，睡眠，或者占用，也就是说阻塞状态，而这时线程被中断就会抛出这类错误,
						但是线程并没有被中断，任何时候，出现该异常都需要手动在代码中再次中断该异常。 
						Java6之后结束某个线程A的方法是A.interrupt()。
						如果这个线程正处于非阻塞状态，比如说线程正在执行某些代码的时候，被interrupt，
						那么该线程的interrupt变量会被置为true，告诉别人说这个线程被中断了（只是一个标志位，这个变量本身并不影响线程的中断与否），
						而且线程会被中断，这时不会有interruptedException。但如果这时线程被阻塞了，比如说正在睡眠，那么就会抛出这个错误。
						
				

 * 
 */