package com.bdsoft.crawler;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 测试：倒数计时器栅栏
 */
public class CountDownLatchDemo {

	static final int SIZE = 50;

	/**
	 * 入口
	 */
	public static void main(String[] args) {
		CountDownLatch cdl = new CountDownLatch(SIZE);
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			es.execute(new WaitingTask(cdl));
		}
		for (int i = 0; i < SIZE; i++) {
			es.execute(new TaskPortion(cdl));
		}
		es.shutdown();
	}

}

// 执行任务线程
class TaskPortion implements Runnable {

	private static int counter = 0;
	private final int id = counter++;
	private static Random rad = new Random(1988);
	private final CountDownLatch latch;

	public TaskPortion(CountDownLatch cdl) {
		latch = cdl;
	}

	public void run() {
		try {
			doTask();
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doTask() throws Exception {
		TimeUnit.MILLISECONDS.sleep(rad.nextInt(2000));
		System.out.println(this + " 任务执行完毕");
	}

	public String toString() {
		return String.format("TaskPortion#%1$-3d", id);
	}
}

// 等待任务执行完成线程
class WaitingTask implements Runnable {

	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;

	public WaitingTask(CountDownLatch cdl) {
		latch = cdl;
	}

	public void run() {
		try {
			latch.await();
			System.out.println("倒计时计数器完成通知：" + this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return String.format("WaitingTask#%1$-3d", id);
	}
}
