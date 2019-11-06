package com.study.day06;

public final class ReadWriteLock {
	private int ReadingReaders =0;
	private int WaitingWriters =0;
	private int WritingWriters =0;
	private boolean preferWriter =true;
	
	public synchronized void readLock(){
			try {
				/*conflict(读取-写入) / (读取-读取)
				 * if(正在写入数<=0){	//没有线程正在进行写入操作  WritingWriters
				 * 	读取ing...
				 * }
				 * */
				while(WritingWriters>0 || (preferWriter && WritingWriters>0)){
				wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				ReadingReaders++;
			}
	}
		
	public synchronized void readUnlock(){
		ReadingReaders--;
		preferWriter =true;
		notifyAll();
	}
	public synchronized void writeLock(){
			WaitingWriters++;
			 try {
			   /*conflict(读取-写入) / conflict(写入-写入)
				* if(正在读取数<=0 && 正在写入数<=0){	 //没有线程正在进行读取操作或写入操作 ReadingReaders/WritingWriters
				* 	写入ing...
				* }
				* */
				while(ReadingReaders>0 || WritingWriters>0){
				wait();
				}
			 }catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				WaitingWriters--;
			}
			 WritingWriters++;	 
	}
	public synchronized void writeUnlock(){
		 WritingWriters--;
		 preferWriter =false;
		 notifyAll();
	}
}
