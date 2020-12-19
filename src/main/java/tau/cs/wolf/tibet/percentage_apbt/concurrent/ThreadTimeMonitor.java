package tau.cs.wolf.tibet.percentage_apbt.concurrent;

import org.joda.time.Duration;
import org.slf4j.Logger;

import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;

public class ThreadTimeMonitor extends Thread {
	
	private final Logger monitorLogger;
	private final Thread thread;
	private final long pollTime;
	private final String threadName;

	public ThreadTimeMonitor(Logger monitorLogger, Thread thread, Duration pollTime, String threadName) {
		super(threadName+" monitor");
		this.monitorLogger = monitorLogger;
		this.thread = thread;
		this.pollTime = pollTime.getMillis();
		this.threadName = threadName;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		while (true) {
			try {
				Thread.sleep(this.pollTime);
				if (!thread.isAlive()) {
					break;
				}
				monitorLogger.info(String.format("Thread %s elapsed for %s", this.threadName, Utils.formatDuration(System.currentTimeMillis() - startTime)));
			} catch(InterruptedException e) {
				return;
			}

		}
	}

}