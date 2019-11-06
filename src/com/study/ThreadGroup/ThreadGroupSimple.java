package com.study.ThreadGroup;

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
public class ThreadGroupSimple extends ThreadGroup{

	public ThreadGroupSimple(String name) {
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
        
        Thread thread1 = new Thread(new MyThread(), "thread_01");
        Thread thread2 = new Thread(new MyThread(), "thread_02");
        
        System.out.println(thread1.getThreadGroup().getName());
        System.out.println(thread2.getThreadGroup().getName());
        
        System.out.println("-------");
        
        ThreadGroup myGroup = new ThreadGroup("demo1线程组");// 创建新的线程组
        Thread thread3 = new Thread(myGroup, new MyThread(), "thread_03");
        Thread thread4 = new Thread(myGroup, new MyThread(), "thread_04");
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
		ThreadA aRunnable = new ThreadA();
		ThreadB bRunnable = new ThreadB();

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

class MyThread implements Runnable{

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			System.out.println(Thread.currentThread().getName()+"... "+i);
		}
	}
	
}

class ThreadA extends Thread {
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

class ThreadB extends Thread {
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
