package com.study.CollectionSync.ArrayListSync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.study.day01.main;

public class Main {
	
	public static void main(String[] args) {
		
		List<Integer> list = new ArrayList<Integer>();
		//final List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
		new WriterThread(list).start();
		new ReaderThread(list).start();
	}

}
