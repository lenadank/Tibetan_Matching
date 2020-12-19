package tau.cs.wolf.tibet.percentage_apbt.main.args;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kohsuke.args4j.CmdLineException;

public final class ArgsUtils {

	public static void assertIsFraction(float f, String optionName) throws CmdLineException {
		if (f<0.0 || f>1.0) {
			throwCmdLineException(optionName, "must be a fraction");
		}

	}

	public static void assertFileExists(File f, String optionName) throws CmdLineException {
		if (!f.isFile()) {
			throwCmdLineException(optionName, "file does not exist: "+f.getPath());
		}
	}
	
	public static void assertDirExists(File dir, String optionName) throws CmdLineException {
		if (!dir.isDirectory()) {
			throwCmdLineException(optionName, "directory does not exist: "+dir.getPath());
		}
	}
	
	public static void assertDirExists(Path dir, String optionName) throws CmdLineException {
		if (!Files.isDirectory(dir)) {
			throwCmdLineException(optionName, "directory does not exist: "+dir.toAbsolutePath().toString());
		}
	}

	public static void assertIsNonNegative(int num, String optionName) throws CmdLineException {
		if (num < 0) {
			throwCmdLineException(optionName, "must be a non-negative integer");
		}
	}

	@SuppressWarnings("deprecation")
	private static void throwCmdLineException(String optionName, String msg) throws CmdLineException {
		throw new CmdLineException("Option "+optionName+": "+msg);
	}

	public static <T extends Enum<T>> EnumSet<T> p(Class<T> t) {
		return EnumSet.allOf(t);
	}
	
	@SuppressWarnings("deprecation")
	public static <T extends Enum<T>> T parseEnum(Class<T> t, String val) throws CmdLineException {
		try {
			return Enum.valueOf(t, val);
		} catch (IllegalArgumentException e) {
			;
			throw new CmdLineException(String.format("Illegal value for enum %s: %s\nPossible values: %s",t.getSimpleName(), val, EnumSet.allOf(t).toString()));
		}
		
	}

	public static SortedSet<Path> getAllPaths(Path rootPath, String pathPattern) {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath, pathPattern)) {
			SortedSet<Path> res = new TreeSet<>();
			for (Path path: ds) {
				res.add(path);
			}
			return res;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
