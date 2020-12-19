package tau.cs.wolf.tibet.percentage_apbt.misc;

public class ClosingRunnable implements Runnable {
	private final Runnable runnable;
	private final AutoCloseable closeable;
	
	public ClosingRunnable(Runnable runnable, AutoCloseable closeable) {
		this.runnable = runnable;
		this.closeable = closeable;
	}
	
	@Override
	public void run() {
		try {
			this.runnable.run();
		} finally {
			try {
				this.closeable.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}