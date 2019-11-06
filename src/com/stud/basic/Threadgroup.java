package com.stud.basic;

/*
 * 线程组 ThreadGroup
 * 可以把线程归属到某一个线程组中， 线程组中可以有线程对象， 也可以有线程组， 组中还可以有线程。 
 * 这样的组织结构有些类似于树的形式：
				 				系统线程组(system)
				    				|
				 	main线程  Thread1 Thread2  线程组1  线程组2
											  |
				 			Thread3 Thread4  线程组3
											   |
				 						线程组4 Thread5
 * 
 * 线程对象关联线程组 ： 1级关联
		 所谓的 1 级关联就是父对象中有子对象， 但并不创建子孙对象。 这种情况经常出现在开发中，
		 比如创建一些线程时， 为了有效地对这些线程进行组织管理， 通常的情况下是创建一个线程组， 
		 然后再将部分线程归属到该组中。 这样的处理可以对零散的线程对象进行有效的组织与规划。
 
 * 线程对象关联线程组 ： 多级关联
		所谓的多级关联就是父对象中有子对象， 子对象中再创建子对象， 也就是出现子孙对象的效果了。 
		但是此种写法在开发中不太常见， 如果线程树结构设计得非常复杂反而不利于线程对象的管理， 
		但却提供了支持多级关联的线程树结构。
		
 * 线程组自动归属特性
		自动归属就是自动归到当前线程组中。
 * 
 * API介绍
		Thread类
				Thread(ThreadGroup group,Runnable target)：group属于的线程组，target为新线程
				Thread(ThreadGroup group,Runnable target,String name)：group属于的线程组，target为新线程，name：线程名
				Thread(ThreadGroup group,String name)：新线程名为name，属于group线程组

		ThreadGroup类
			构造方法
				ThreadGroup(String name)：以指定线程组名字来创建新线程组
				ThreadGroup(ThreadGroup parent,String name)：以指定的名字、指定的父线程组来创建一个新线程组。
			常用操作方法
				int activeCount()：获取线程组中活动线程的数量
				int activeGroupCount()   返回此线程组中活动线程组的估计数
				interrupt()：中断线程组中所有线程
				isDaemon()：是否为后台线程组
				setDaemon(boolean daemon)：设置为后台线程组
				setMaxPriority(int pri)：设置线程组的最高优先级
				int activeCount() 

 * */
public class Threadgroup extends ThreadGroup{

	public Threadgroup(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
//		demo1();
//		demo2();// 线程对象关联线程组 ： 1级关联
//		demo3();// 线程对象关联线程组 ： 多级关联
//		demo4();// 线程组自动归属特性
//		demo5();// 获取根线程组
//		demo6();// 线程组里加线程组
//		demo7();// 批量停止组内线程
		demo8();// 递归与非递归取得组内对象
	}

	private static void demo1() {
		ThreadGroup mainGroup  = Thread.currentThread().getThreadGroup();
		System.out.println("主线程组的名字：" + mainGroup.getName());
        System.out.println("主线程组是否是后台线程组：" + mainGroup.isDaemon());
        
        System.out.println("-------");
        
        Thread thread1 = new Thread(new MyThread1(), "thread_01");
        Thread thread2 = new Thread(new MyThread1(), "thread_02");
        
        System.out.println(thread1.getThreadGroup().getName());
        System.out.println(thread2.getThreadGroup().getName());
        
        System.out.println("-------");
        
        ThreadGroup myGroup = new ThreadGroup("demo1线程组");// 创建新的线程组
        Thread thread3 = new Thread(myGroup, new MyThread1(), "thread_03");
        Thread thread4 = new Thread(myGroup, new MyThread1(), "thread_04");
        System.out.println(thread3.getThreadGroup().getName());
        System.out.println(thread4.getThreadGroup().getName());
        
        /*
         *  主线程组的名字：main
			主线程组是否是后台线程组：false
			-------
			main
			main
			-------
			demo1线程组
			demo1线程组
         * */
        
	}

	private static void demo2() {
		ThreadA1 aRunnable = new ThreadA1();
		ThreadB1 bRunnable = new ThreadB1();

		ThreadGroup group = new ThreadGroup("demo2线程组");

		Thread aThread = new Thread(group, aRunnable);
		Thread bThread = new Thread(group, bRunnable);
		aThread.start();
		bThread.start();

		System.out.println("活动的线程数为：" + group.activeCount());
		System.out.println("当前线程组: "+Thread.currentThread().getThreadGroup().getName()+
							",活跃线程数: "+Thread.currentThread().getThreadGroup().activeGroupCount());
		System.out.println("线程组的名称为：" + group.getName());
		
		/*
		 *  活动的线程数为：2
			ThreadName=Thread-2
			ThreadName=Thread-3
			线程组的名称为：demo2线程组
			ThreadName=Thread-3
			ThreadName=Thread-2
			...
		 * */

	}
	
	public static void demo3() {

		// 在main组中添加一个线程组A，然后在这个A组中添加线程对象Z
		// 方法activeGroupCount()和activeCount()的值不是固定的
		// 是系统中环境的一个快照
		// .enumerate(Thread[] list) 把此线程组及其子组中的所有活动线程复制到指定数组中。
		ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup group = new ThreadGroup(mainGroup, "A");//以指定的名字A、指定的父线程组mainGroup来创建一个新线程组。
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("runMethod!");
					Thread.sleep(10000);// 线程必须在运行状态才可以受组管理
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread newThread = new Thread(group, runnable);
		newThread.setName("Z");
		newThread.start();// 线程必须启动然后才归到组A中
		// ///
		ThreadGroup[] listGroup = new ThreadGroup[Thread.currentThread()
				.getThreadGroup().activeGroupCount()];
		
		Thread.currentThread().getThreadGroup().enumerate(listGroup);//复制该线程组及其子组中的所有活动线程到指定的数组。
		System.out.println("main线程中有多少个子线程组：" + listGroup.length + " 名字为："
				+ listGroup[0].getName());
		
		Thread[] listThread = new Thread[listGroup[0].activeCount()];
		
		listGroup[0].enumerate(listThread);
		System.out.println(listThread[0].getName());

	}

	//在mian线程中，不为线程组明确指定线程，就默认放当前线程中
	public static void demo4() {
		System.out.println("A处线程："+Thread.currentThread().getName()+
				" 中有线程组数量："+Thread.currentThread().getThreadGroup().activeGroupCount());
		ThreadGroup group=new ThreadGroup("新的组");
		System.out.println("A处线程："+Thread.currentThread().getName()+
				" 中有线程组数量："+Thread.currentThread().getThreadGroup().activeGroupCount());
		ThreadGroup[] threadGroup=new ThreadGroup[Thread.currentThread().getThreadGroup().activeGroupCount()];
		Thread.currentThread().getThreadGroup().enumerate(threadGroup);
		for (int i = 0; i < threadGroup.length; i++) {
			System.out.println("第一个线程组名称为："+threadGroup[i].getName());
		}
	}
	
	//main线程属于默认的main线程组，mian线程组的父线程组为system，system为顶级线程组
	public static void demo5() {
		System.out.println("线程：" + Thread.currentThread().getName()
				+ " 所在的线程组名为："
				+ Thread.currentThread().getThreadGroup().getName());
		System.out
				.println("main线程所在的线程组的父线程组的名称是："
						+ Thread.currentThread().getThreadGroup().getParent()
								.getName());
		System.out.println("main线程所在的线程组的父线程组的父线程组的名称是："
				+ Thread.currentThread().getThreadGroup().getParent()
						.getParent().getName());
	}
	
	//main线程组中默认没有线程组，有活跃线程main。
	public static void demo6() {

		System.out.println("线程组名称："
				+ Thread.currentThread().getThreadGroup().getName());
		System.out.println("线程组中活动的线程数量："
				+ Thread.currentThread().getThreadGroup().activeCount());
		System.out.println("线程组中线程组的数量-加之前："
				+ Thread.currentThread().getThreadGroup().activeGroupCount());
		ThreadGroup newGroup = new ThreadGroup(Thread.currentThread()
				.getThreadGroup(), "newGroup");
		System.out.println("线程组中线程组的数量-加之之后："
				+ Thread.currentThread().getThreadGroup().activeGroupCount());
		System.out
				.println("父线程组名称："
						+ Thread.currentThread().getThreadGroup().getParent()
								.getName());
	}
	
	public static void demo7() {
		try {
			ThreadGroup group = new ThreadGroup("我的线程组");
			for (int i = 0; i < 5; i++) {
				ThreadStop thread = new ThreadStop(group, "线程" + (i + 1));
				thread.start();
			}
			Thread.sleep(5000);
			group.interrupt();
			System.out.println("调用了interrupt()方法");
		} catch (InterruptedException e) {
			System.out.println("停了停了！");
			e.printStackTrace();
		}
	}

	public static void demo8() {
		ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup groupA = new ThreadGroup(mainGroup, "A");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("runMethod!");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadGroup groupB = new ThreadGroup(groupA, "B");

		// 分配空间，但不一定全部用完
		ThreadGroup[] listGroup1 = new ThreadGroup[Thread.currentThread()
				.getThreadGroup().activeGroupCount()];
		System.out.println("当前活跃组数量： "+listGroup1.length);
		// 传入true 是递归取得子组及子孙组 
		Thread.currentThread().getThreadGroup().enumerate(listGroup1, true);
		for (int i = 0; i < listGroup1.length; i++) {
			if (listGroup1[i] != null) {
				System.out.println("enumerate...true: "+listGroup1[i].getName());
			}
		}
		
		/*enumerate(ThreadGroup[] list,boolean recurse)
		 * 把对此线程组中的所有活动子组的引用复制到指定数组中。如果 recurse 标志为 true，则还包括对子组的所有活动子组的引用。*/
		System.out.println("-----------");
		
		ThreadGroup[] listGroup2 = new ThreadGroup[Thread.currentThread()
				.getThreadGroup().activeGroupCount()];
		System.out.println("当前活跃组数量： "+listGroup1.length);
		// 非递归取得子对象，也就是不取得子组线程
		Thread.currentThread().getThreadGroup().enumerate(listGroup2, false);
		for (int i = 0; i < listGroup2.length; i++) {
			if (listGroup2[i] != null) {
				System.out.println("enumerate...false: "+listGroup2[i].getName());
			}
		}
	}
}

class MyThread1 implements Runnable{

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			System.out.println(Thread.currentThread().getName()+"... "+i);
		}
	}
	
}

class ThreadA1 extends Thread {
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("ThreadName=" + Thread.currentThread().getName());
				Thread.sleep(3000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ThreadB1 extends Thread {
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("ThreadName=" + Thread.currentThread().getName());
				Thread.sleep(3000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ThreadStop extends Thread {
	public ThreadStop(ThreadGroup group, String name) {
		super(group, name);
	}
	@Override
	public void run() {
		System.out.println("ThreadName=" + Thread.currentThread().getName()
				+ "准备开始死循环了：)");
		while (!this.isInterrupted()) {
		}
		System.out.println("ThreadName=" + Thread.currentThread().getName()
				+ "结束了：)");
	}
}

/*
 * Java中守护线程的总结 (Daemon)
 * 
 * 在Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程) ，用个比较通俗的比方，任何一个守护线程都是整个JVM中所有非守护线程的保姆：
 * 只要当前JVM实例中尚存在任何一个非守护线程没有结束，守护线程就全部工作；只有当最后一个非守护线程结束时，守护线程随着JVM一同结束工作。
 * Daemon的作用是为其他线程的运行提供便利服务，守护线程最典型的应用就是 GC (垃圾回收器)，它就是一个很称职的守护者。
 * User和 Daemon 两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：如果 User Thread已经全部退出运行了，只剩下Daemon Thread存在了，虚拟机也就退出了。 
 * 因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必要了。
 * 值得一提的是，守护线程并非只有虚拟机内部提供，用户在编写程序时也可以自己设置守护线程。下面的方法就是用来设置守护线程的：
 * 
		 Thread daemonTread = new Thread();
		 daemonThread.setDaemon(true);    // 设定 daemonThread 为 守护线程，default false(非守护线程)
		 daemonThread.isDaemon();		 //  验证当前线程是否为守护线程，返回 true 则为守护线程
 * 
		 * 这里有几点需要注意：
		(1) thread.setDaemon(true) 必须在thread.start()之前设置，否则会跑出一个IllegalThreadStateException异常。你不能把正在运行的常规线程设置为守护线程。
		(2) 在Daemon线程中产生的新线程也是Daemon的。
		(3) 不要认为所有的应用都可以分配给Daemon来进行服务，比如读写操作或者计算逻辑。
		
				因为你不可能知道在所有的User完成之前，Daemon是否已经完成了预期的服务任务。
				一旦User退出了，可能大量数据还没有来得及读入或写出，计算任务也可能多次运行结果不一样。这对程序是毁灭性的。
				造成这个结果理由已经说过了：一旦所有User Thread离开了，虚拟机也就退出运行了。 
				
					//完成文件输出的守护线程任务
					import java.io.*;   
					  
					class TestRunnable implements Runnable{   
					    public void run(){   
					               try{   
					                  Thread.sleep(1000);//守护线程阻塞1秒后运行   
					                  File f=new File("daemon.txt");   
					                  FileOutputStream os=new FileOutputStream(f,true);   
					                  os.write("daemon".getBytes());   
					           }   
					               catch(IOException e1){   
					          		  e1.printStackTrace();   
					               }   
					               catch(InterruptedException e2){   
					                  e2.printStackTrace();   
					           }   
					    }   
					}   
					public class TestDemo2{   
					    public static void main(String[] args) throws InterruptedException   
					    {   
					        Runnable tr=new TestRunnable();   
					        Thread thread=new Thread(tr);   
					                thread.setDaemon(true); //设置守护线程   
					        thread.start(); //开始执行分进程   
					    }   
					} 
					  
					>>运行结果：文件daemon.txt中没有"daemon"字符串。
					
					把输入输出逻辑包装进守护线程多么的可怕，字符串并没有写入指定文件。
					原因也很简单，直到主线程完成，守护线程仍处于1秒的阻塞状态。
					这个时候主线程很快就运行完了，虚拟机退出，Daemon停止服务，输出操作自然失败了。
					
					前台线程是保证执行完毕的，后台线程有可能还没有执行完毕就退出了。
					实际上：JRE判断程序是否执行结束的标准是所有的前台执线程行完毕了，而不管后台线程的状态，
					因此，在使用后台线程时候一定要注意这个问题。 
 * 
 * 补充说明:
		定义:
			守护线程--也称“服务线程”，在没有用户线程可服务时会自动离开。
		优先级:
			守护线程的优先级比较低，用于为系统中的其它对象和线程提供服务。
		设置:
			通过setDaemon(true)来设置线程为“守护线程”；将一个用户线程设置为
			守护线程的方式是在线程对象创建 之前 用线程对象的setDaemon方法。
		example: 
			垃圾回收线程就是一个经典的守护线程，当我们的程序中不再有任何运行的Thread,
			程序就不会再产生垃圾，垃圾回收器也就无事可做，所以当垃圾回收线程是JVM上仅剩的线程时，
			垃圾回收线程会自动离开。它始终在低级别的状态中运行，用于实时监控和管理系统中的可回收资源。
		生命周期:
			守护进程（Daemon）是运行在后台的一种特殊进程。
			它独立于控制终端并且周期性地执行某种任务或等待处理某些发生的事件。
			也就是说守护线程不依赖于终端，但是依赖于系统，与系统“同生共死”。
				那Java的守护线程是什么样子的呢?
					当JVM中所有的线程都是守护线程的时候，JVM就可以退出了；
					如果还有一个或以上的非守护线程则JVM不会退出。
 * 
 *为什么要用守护线程？
	我们知道静态变量是ClassLoader级别的，如果Web应用程序停止，这些静态变量也会从JVM中清除。
	但是线程则是JVM级别的，如果你在Web 应用中启动一个线程，这个线程的生命周期并不会和Web应用程序保持同步。
	也就是说，即使你停止了Web应用，这个线程依旧是活跃的。
	正是因为这个很隐晦 的问题，所以很多有经验的开发者不太赞成在Web应用中私自启动线程。
	如果我们手工使用JDK Timer（Quartz的Scheduler），在Web容器启动时启动Timer，
	当Web容器关闭时，除非你手工关闭这个Timer，否则Timer中的任务还会继续运行！
	
		//代码清单StartCycleRunTask：容器监听器
		package com.baobaotao.web;
		import java.util.Date;
		import java.util.Timer;
		import java.util.TimerTask;
		import javax.servlet.ServletContextEvent;
		import javax.servlet.ServletContextListener;
		
		public class StartCycleRunTask implements ServletContextListener ...{
		    private Timer timer;
		    public void contextDestroyed(ServletContextEvent arg0) ...{
		        // ②该方法在Web容器关闭时执行
		        System.out.println("Web应用程序启动关闭...");
		    }
		    public void contextInitialized(ServletContextEvent arg0) ...{
		         //②在Web容器启动时自动执行该方法
		        System.out.println("Web应用程序启动...");
		        timer = new Timer();//②-1:创建一个Timer，Timer内部自动创建一个背景线程
		        TimerTask task = new SimpleTimerTask();
		        timer.schedule(task, 1000L, 5000L); //②-2:注册一个5秒钟运行一次的任务
		    }
		}
		class SimpleTimerTask extends TimerTask ...{//③任务
		    private int count;
		    public void run() ...{
		        System.out.println((++count)+"execute task..."+(new Date()));
		    }
		}
		
		>>在Tomcat中部署这个Web应用并启动后，你将看到任务每隔5秒钟执行一次。
		>>运行一段时间后，登录Tomcat管理后台，将对应的Web应用（chapter13）关闭。
		>>转到Tomcat控制台，你将看到虽然Web应用已经关闭，但Timer任务还在我行我素地执行如故.
		
		我们可以通过改变清单StartCycleRunTask的代码，
		在contextDestroyed(ServletContextEvent arg0)中添加timer.cancel()代码，
		在Web容器关闭后手工停止Timer来结束任务。


  Spring为JDK Timer和Quartz Scheduler所提供的TimerFactoryBean和SchedulerFactoryBean能够和Spring容器的生命周期关联，
     在 Spring容器启动时启动调度器，而在Spring容器关闭时，停止调度器。
     所以在Spring中通过这两个FactoryBean配置调度器，再从 Spring IoC中获取调度器引用进行任务调度将不会出现这种Web容器关闭而任务依然运行的问题。
     而如果你在程序中直接使用Timer或Scheduler，如不 进行额外的处理，将会出现这一问题。 
 * 
 * 
 * */
