package tau.cs.wolf.tibet.percentage_apbt.misc;

import org.apache.log4j.Level;
import org.slf4j.Logger;

public class LoggingExceptionRunnable implements Runnable {

	private final Runnable runnable;
	private final Logger logger;
	
	public LoggingExceptionRunnable(Runnable runnable, Logger logger) {
		this.runnable = runnable;
		this.logger = logger;
	}
	
	@Override
	public void run() {
		try {
			this.runnable.run();
		} catch(Exception e) {
			logger.error("Reporting caught exception: ");
			e.printStackTrace(new LoggerWriter(this.logger, Level.ERROR));
		}
	}
	
}