package com.stud.basic;

/*
 *  线程的合并
		作用是使所属的线程对象X正常执行run()方法中的任务， 
		而使当前线程 Z 进行无限期的阻塞， 等待线程 X 销毁后再继续执行线程 Z 后面的代码。	 
		
		方法 join 具有使线程排队运行的作用， 有些类似同步的运行效果。 
			join 与 synchronized的区别是： 
				join 在内部使用 wait()方法进行等待， 
				而 sychmnized 关键字使用的是 “对象监视器” 原理做为同步
		
		join有3个重载的方法：
			void join():当前线程等该加入该线程后面，等待该线程终止。
			void join(long millis):	当前线程等待该线程终止的时间最长为 millis 毫秒。
									 如果在millis时间内，该线程没有执行完，
									 那么当前线程进入就绪状态，重新等待cpu调度。
			void join(long millis,int nanos):	等待该线程终止的时间最长为 millis 毫秒 + nanos纳秒。
												如果在millis时间内，该线程没有执行完，
												那么当前线程进入就绪状态，重新等待cpu调度。
												
	Join() 方法与异常 (见类Run处)
		在Join() 过程中， 如果当前线程对象被中断， 则当前线程出现异常。


 * */
public class Join extends Thread{

	public static void main(String[] args) throws InterruptedException {

        	MyThreadJoin thread = new MyThreadJoin();
            thread.start();
    		//Thread.sleep(5000);
            thread.join();// 此时当thread对象执行完毕后，main线程才执行
    		System.out.println("我想当thread对象执行完毕后我再执行");
    		System.out.println("但上面代码中的sleep()中的值应该写多少呢？");
    		System.out.println("答案是：根据不能确定:)");
	}
}

class MyThreadJoin extends Thread {
    @Override
    public void run() {
		try {
			int secondValue = (int) (Math.random() * 10000);
			System.out.println(secondValue);
			Thread.sleep(secondValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

//--------------------------------------------------------------------/

/*
 *  但将方法中的代码改成使用 sleep(2000) 方法时， 运行的效果还是等待了 2 秒，
	那使用join(2000) 和使用 sleep(2000) 有什么区别呢？ 示例中在运行效果上并没有
	区别， 其实区别主要还是来自于这 2 个方法对同步的处理上。
	
	方法join(long millis) 与sleep(long millis) 的区别
		方法join(long millis) 的功能在内部是使用 wait(long millis) 方法来实现的， 
		所以join(long millis) 方法具有释放锁的特点。
	
		源码：
			public final synchronized void join(long millis)	//
		    throws InterruptedException {
		        long base = System.currentTimeMillis();
		        long now = 0;
		
		        if (millis < 0) {
		            throw new IllegalArgumentException("timeout value is negative");
		        }
		
		        if (millis == 0) {
		            while (isAlive()) {
		                wait(0);
		            }
		        } else {
		            while (isAlive()) {
		                long delay = millis - now;
		                if (delay <= 0) {
		                    break;
		                }
		                wait(delay);
		                now = System.currentTimeMillis() - base;
		            }
		        }
		    }

			从源代码中可以了解到， 当执行 wait(long millis) 方法后， 当前线程的锁被释放， 
			那么其他线程就可以调用此线程中的同步方法了。而sleep(long millis) 方法却不释放锁。 //测试见185p
			
				 线程A 使用sleep(long millis) 方 法一直持有对象 B的 锁， 时间达到 6 秒， 
				 所以线程C 只有在线程A 时间到达 6 秒后释放线程B 的锁时， 
				 才可以调用线程B 中的同步方法 synchronized public void bService()
				 
				 由于线程 ThreadA 释放了 ThreadB 的锁，
				 所以线程 ThreadC 可以调用 ThreadB 中的同步方法 synchronized public void bService()
				 此实验也再次说明 join(long)方法具有释放锁的特点

		sleep()
		　　   sleep()方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入阻塞状态，
			该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。但是sleep()方法不会释放“锁标志”，
			也就是说如果有synchronized同步块，其他线程仍然不能访问共享数据。
		　　
		wait()
		　　wait()方法需要和notify()及notifyAll()两个方法一起介绍，这三个方法用于协调多个线程对共享数据的存取，
		       所以必须在synchronized语句块内使用，也就是说，调用wait()，notify()和notifyAll()的任务在调用这些方法前必须拥有对象的锁。
		       注意，它们都是Object类的方法，而不是Thread类的方法。
		　　wait()方法与sleep()方法的不同之处在于，wait()方法会释放对象的 锁标志。
		       当调用某一对象的wait()方法后，会使当前线程暂停执行，并将当前线程放入对象等待池中，
		       直到调用了notify()方法后，将从对象等待池中移出任意一个线程并放入锁标志等待池中，等待争取锁
		       只有锁标志等待池中的线程可以获取锁标志，它们随时准备争夺锁的拥有权。
		       当调用了某个对象的notifyAll()方法，会将对象等待池中的所有线程都移动到该对象的锁标志等待池。
		　　除了使用notify()和notifyAll()方法，还可以使用带毫秒参数的wait(long timeout)方法，
		       效果是在延迟timeout毫秒后，被暂停的线程将被恢复到锁标志等待池。
		　　此外，wait()，notify()及notifyAll()只能在synchronized语句中使用，
		
		yield()
		　　	yield()方法和sleep()方法类似，也不会释放锁标志，区别在于，它没有参数，(放弃当前CPU资源)
			即yield()方法只是使当前线程重新回到可执行状态，所以执行yield()的线程有可能在进入到可执行状态后马上又被执行，
			另外yield()方法只能使同优先级或者高优先级的线程得到执行机会，这也和sleep()方法不同。


 * 
 * */
class MyThreadJoinLong extends Thread {

	@Override
	public void run() {
		try {
			System.out.println("begin Timer=" + System.currentTimeMillis());
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Test {
	public static void main(String[] args) {
		try {
			MyThreadJoinLong threadTest = new MyThreadJoinLong();
			threadTest.start();

			//threadTest.join(2000);// 只等2秒
			Thread.sleep(2000);

			System.out.println("  end timer=" + System.currentTimeMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
			/*	>>begin Timer=1568983534800
			  	>>end timer=1568983536800	*/
}



//--------------------------------------------------------------------/

class ThreadA extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String newString = new String();
			Math.random();
		}
	}
}

class ThreadB extends Thread {
	@Override
	public void run() {
		try {
			ThreadA a = new ThreadA();
			a.start();
			a.join();

			System.out.println("线程B在run end处打印了");
		} catch (InterruptedException e) {
			System.out.println("线程B在catch处打印了");
			e.printStackTrace();
		}
	}
}

class ThreadC extends Thread {
	private ThreadB threadB;

	public ThreadC(ThreadB threadB) {
		super();
		this.threadB = threadB;
	}
	@Override
	public void run() {
		threadB.interrupt();
	}
}

/*
 * 说明方法join() 与interrupt() 方法如果彼此遇到， 则会出现异常。 
 * 但进程按钮还呈红色 ， 原因是线程还在继续运行，线程并未出现异常，是正常执行的状态
 * 
 * */
 class Run {
	public static void main(String[] args) {

		try {
			ThreadB b = new ThreadB();
			b.start();

			Thread.sleep(500);

			ThreadC c = new ThreadC(b);
			c.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
 
//--------------------------------------------------------------------/


