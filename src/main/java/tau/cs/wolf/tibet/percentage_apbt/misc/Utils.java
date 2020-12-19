package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;

import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;

public class Utils {
	public static interface Formatter<T> {
		public String format(T t);
	}
	
	public static interface Parser<T> {
		public T parse(String str);
	}

	public static String formatDuration(long milliseconds) {
		long seconds = milliseconds / 1000;
		return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
	}

	public static String readFile(File f) {
		try(FileInputStream stream = new FileInputStream(f)) {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
			//return Charset.defaultCharset().toString();
		} catch(IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static String gobbleInputStream(InputStream in) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer);
		return writer.toString();
	}

	public static File urlToFile(URL url) {
		File f;
		try {
			f = new File(url.toURI());
		} catch(URISyntaxException e) {
			f = new File(url.getPath());
		}
		return f;
	}
	
	public static void writeMatches(File f, List<MatchResult> matches, Formatter<MatchResult> formatter) {
		writeMatches(f, matches, formatter, false);
		writeMatches(new File(f.getPath().replace(".txt", "")+".sorted.txt"), matches, formatter, true);
	}
	
	private static void writeMatches(File f, List<MatchResult> matches, Formatter<MatchResult> formatter, boolean sort) {
		if (sort){
			Collections.sort(matches, new MatchResultComparator());
		}
		if (formatter == null) {
			formatter = new MatchResult.DefaultFormatter();
		}
		try (PrintStream out = new PrintStream(f)) {
			for (MatchResult match: matches) {
				out.println(formatter.format(match));
			}
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}

	}
	
	public static void reportComputationTimeByStartTime(Logger logger, long startTime, String msg) {
		logger.info(msg+". Time elapsed: "+Utils.formatDuration(System.currentTimeMillis()-startTime));
	}
	
	public static void reportComputationTimeByDuration(Logger logger, long duration, String msg) {
		logger.info(msg+". Time elapsed: "+Utils.formatDuration(duration));
	}

	public static class OutputStreamGobbler {
		private final ByteArrayOutputStream baos;
		
		public OutputStreamGobbler() {
			this.baos = new ByteArrayOutputStream();
		}
		
		public OutputStream get() {
			return this.baos;
		}
		
		@Override
		public String toString() {
			return baos.toString(); 
		}
		
	}
	
	public static class MatchResultComparator implements Comparator<MatchResult>{

		@Override
		public int compare(MatchResult o1, MatchResult o2) {
			return Double.compare(o2.getScore(), o1.getScore());
		}
		
	}
	
	public static class PatternFileFilter implements IOFileFilter {

		private final Pattern pattern;
		
		public PatternFileFilter(Pattern pattern) {
			this.pattern = pattern;
		}
		
		@Override
		public boolean accept(File file) {
			return accept(file.getName());
		}

		@Override
		public boolean accept(File dir, String name) {
			return accept(name);
		}
		
		private boolean accept(String name) {
			Matcher m = this.pattern.matcher(name); 
			return m.matches();
		}

		
	}

	public static int min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	
	public static int max(Integer... ints ) {
		if (ints.length == 0) {
			throw new IllegalArgumentException("must have at least one value");
		}
		return recursiveMax(0, ints.length, ints);
	}
	
	private static int recursiveMax(int startIdx, int endIdx, Integer... ints) {
		int length = endIdx - startIdx;
		switch (length) {
		case 0: return Integer.MIN_VALUE;
		case 1: return ints[startIdx];
		default: 
			int midIdx = startIdx + length/2;
			return Math.max(recursiveMax(startIdx, midIdx, ints), recursiveMax(midIdx, endIdx, ints));
		}
	}

	public static Properties propsFromVmArg(String vmArgName, boolean mandatory) {
		String pathStr = System.getProperty(vmArgName);
		if (pathStr == null) {
			if (mandatory) {
				throw new IllegalArgumentException("no JVM property: "+vmArgName);
			}
			return new Properties();
		}
		File propsFile = new File(pathStr);
		if (!propsFile.isFile()) {
			throw new IllegalArgumentException("vm argument '"+vmArgName+"' does not point to an existing file!");
		}
		try {
			try (InputStream is = new FileInputStream(propsFile)) {
				Properties props = new Properties();
				props.load(is);
				return props;
			}
		} catch (IOException e) {
			throw new IllegalStateException("Should not have reached this code line");
		}
		
	}
	
	public static int toBytes(String filesize) {
	    int returnValue = -1;
	    Pattern patt = Pattern.compile("([\\d.]+)([GMK]B?)", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = patt.matcher(filesize);
	    Map<String, Integer> powerMap = new HashMap<String, Integer>();
	    powerMap.put("GB", 3);
	    powerMap.put("G", 3);
	    powerMap.put("MB", 2);
	    powerMap.put("M", 2);
	    powerMap.put("KB", 1);
	    powerMap.put("K", 1);
	    if (matcher.find()) {
	      String number = matcher.group(1);
	      int pow = powerMap.get(matcher.group(2).toUpperCase());
	      BigDecimal bytes = new BigDecimal(number);
	      bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow));
	      returnValue = bytes.intValue();
	    }
	    return returnValue;
	}
	
	public static void main(String[] args) {
		System.out.println((toBytes("0.96k")));
	}
	
}
