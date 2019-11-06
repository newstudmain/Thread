package com.study.ThreadGroup;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPoolSimpleServer {
	private int port =8000;
	private ServerSocket server;
	private ThreadPoolSimple threadPool;
	private final int POOL_SIZE	 =4;
	
	public static void main(String[] args) {
		
	}
	
	@SuppressWarnings("resource")
	public ThreadPoolSimpleServer() {
		try {
			server = new ServerSocket(port);
			threadPool = new ThreadPoolSimple(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
			System.out.println("服务器已经启动...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void service() throws IOException {
		Socket socket =null;
		while(true) {
			socket = server.accept();
			//threadPool.execute();
		}
	}
}
