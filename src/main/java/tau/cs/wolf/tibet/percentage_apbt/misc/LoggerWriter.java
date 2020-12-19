package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.log4j.Level;
import org.slf4j.Logger;

/**
 * A subclass of PrintWriter that redirects its output to a slf4j Logger.
 * <p>
 *
 * This class is used to have something to give api methods that require a
 * PrintWriter for logging. JBoss-owned classes of this nature generally ignore
 * the PrintWriter and do their own slf4j logging.
 *
 */
public class LoggerWriter extends PrintWriter {

	/**
	 * Redirect logging to the indicated logger using Level.INFO
	 *
	 * @param logger
	 *            Description of Parameter
	 */
	public LoggerWriter(final Logger logger) {
		this(logger, Level.INFO);
	}

	/**
	 * Redirect logging to the indicated logger using the given level. The ps is
	 * simply passed to super but is not used.
	 *
	 * @param logger
	 *            Description of Parameter
	 * @param level
	 *            Description of Parameter
	 */
	public LoggerWriter(final Logger logger, final Level level) {
		super(new InternalLoggerWriter(new LevelAdapter(logger, level)), true);
	}

	/**
	 * @created August 19, 2001
	 */
	static class InternalLoggerWriter extends Writer {
		private LevelAdapter levelAdapter;
		private boolean closed;

		public InternalLoggerWriter(final LevelAdapter levelAdapter) {
			lock = levelAdapter;
			// synchronize on this logger
			this.levelAdapter = levelAdapter;
		}

		public void write(char[] cbuf, int off, int len) throws IOException {
			if (closed) {
				throw new IOException("Called write on closed Writer");
			}
			// Remove the end of line chars
			while (len > 0 && (cbuf[len - 1] == '\n' || cbuf[len - 1] == '\r')) {
				len--;
			}
			if (len > 0) {
				this.levelAdapter.log(String.copyValueOf(cbuf, off, len));
			}
		}

		public void flush() throws IOException {
			if (closed) {
				throw new IOException("Called flush on closed Writer");
			}
		}

		public void close() {
			closed = true;
		}
	}

	 private static class LevelAdapter {
		 private final Level level;
		 private final Logger logger;
		 
		 public LevelAdapter(Logger logger, Level level) {
			 this.logger = logger;
			 this.level = level;
		 }
		 
		 public void log(String str) {
			 switch(this.level.toInt()) {
			 case Level.TRACE_INT:
				 this.logger.trace(str);
				 break;
			 case Level.DEBUG_INT:
				 this.logger.debug(str);
				 break;
			 case Level.INFO_INT:
				 this.logger.info(str);
				 break;
			 case Level.WARN_INT:
				 this.logger.warn(str);
				 break;
			 case Level.ERROR_INT:
				 this.logger.error(str);
				 break;
			default:
				throw new IllegalArgumentException("Unknown level: "+this.level);
			 }
		 }
	 }

}