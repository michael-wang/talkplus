package com.talkplus.util;

public class Task {

	public static void async(Runnable task) {
		new Thread(task).start();
	}

}
