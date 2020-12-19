package tau.cs.wolf.tibet.percentage_apbt.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolExecotorMonitor implements Runnable  {

	private final Logger monitorLogger;
	private final ThreadPoolExecutor executor;
	private final long pollTime;

	public ThreadPoolExecotorMonitor(Logger monitorLogger, ThreadPoolExecutor executor, Duration duration) {
		this.monitorLogger = monitorLogger==null ? LoggerFactory.getLogger(getClass()) : monitorLogger;
		this.executor = executor;
		this.pollTime = duration.getMillis();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(this.pollTime);
				if (executor.isTerminated()) {
					break;
				}
				monitorLogger.info(String.format("Completed %d/%d tasks", this.executor.getCompletedTaskCount(), this.executor.getTaskCount()));
			} catch(InterruptedException e) {
				throw new IllegalStateException("This thread should never be terminated");
			}

		}
	}

}