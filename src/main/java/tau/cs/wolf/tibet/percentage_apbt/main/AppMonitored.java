package tau.cs.wolf.tibet.percentage_apbt.main;

import org.kohsuke.args4j.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.concurrent.ThreadTimeMonitor;
import tau.cs.wolf.tibet.percentage_apbt.data.AppResults;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsMain;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class AppMonitored extends AppCommon {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		try {
			AppCommon app = new AppMonitored(args);
			app.run();
			app.writeResults();
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	private final AppCommon monitoredApp;
	
	public AppMonitored(ArgsMain args, Props props) {
		super(args, props);
		this.monitoredApp = new AppMain(this.args, this.props);
	}

	private AppMonitored(String[] args) throws CmdLineException {
		this(new ArgsMain(args), null);
	}
	
	@Override
	protected AppResults calcResults() {
		Thread appThread = new Thread(this.monitoredApp, "App");
		final ThreadTimeMonitor monitor = new ThreadTimeMonitor(logger, appThread, args.getPollDuration(), appThread.getName());
		Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
		    	monitor.interrupt();
		    	ex.printStackTrace();
		    	throw new IllegalStateException(ex);
		    }
		};
		appThread.setUncaughtExceptionHandler(handler);
		
		monitor.start();
		logger.info("starting app thread with timeout: "+args.getTimeout());
		appThread.start();
		try {
			appThread.join();
			return this.monitoredApp.getResults();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		} finally {
			monitor.interrupt();
			try {
				monitor.join();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	protected void _writeResults(AppResults results) {
		this.monitoredApp.writeResults();
	}

}
