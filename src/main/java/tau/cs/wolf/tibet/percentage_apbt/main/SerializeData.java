package tau.cs.wolf.tibet.percentage_apbt.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableEntry;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParser;
import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.DataType;
import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.SrcType;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsBase;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsUtils;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;

public class SerializeData implements Runnable {
	
	private final ArgsSerialize args;
	
	public SerializeData(ArgsSerialize argsSerialize) {
		this.args = argsSerialize;
	}

	@Override
	public void run() {
		FileFilter filter = this.args.getFilenamePattern()==null? null : new Utils.PatternFileFilter(this.args.getFilenamePattern());
		File[] files = this.args.getInDir().listFiles(filter);
		@SuppressWarnings("unchecked")
		SlicableParser<?, File> parser = (SlicableParser<?, File>) AppUtils.getParser(args.getDataType(), SrcType.FILE);
		
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(this.args.getOutFile()))) {
			os.writeInt(files.length);
			for (File f: files) {
				Slicable<?> slice = parser.parse(f);
				SlicableEntry entry = new SlicableEntry(f.getPath(), slice);
				os.writeObject(entry);
				System.out.println("successfuly serialized "+f.getPath());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static void main(String[] args) throws CmdLineException {
		new SerializeData(new ArgsSerialize(args)).run();
	}
	
	private static class ArgsSerialize extends ArgsBase {
		private static final long serialVersionUID = 1L;
		
		public ArgsSerialize(String[] args) throws CmdLineException {
			super(args);
		}
		
		private File inDir;
		@SuppressWarnings("deprecation")
		@Option(name = "-in", required=true,  metaVar = "DIR", usage = "input directory")
		public void setInFile1(File f) throws CmdLineException {
			if (!f.isDirectory()) {
				throw new CmdLineException("Option -in dir does not exist: "+f.getPath());
			}
			this.inDir = f;
		}
		public File getInDir() {
			return inDir;
		}
		
		private Pattern filenamePattern;
		@SuppressWarnings("deprecation")
		@Option(name = "-p", aliases={"--pattern"}, metaVar = "REGEX", usage = "pattern to filter file names")
		public void setFilenamePattern(String patternStr) throws CmdLineException {
			try {
				this.filenamePattern = Pattern.compile(patternStr);
			} catch(PatternSyntaxException e) {
				throw new CmdLineException("Error parsing REGEX for -p option", e);
			}
		}
		public Pattern getFilenamePattern() {
			return this.filenamePattern;
		}
		
		private DataType dataType;
		@Option(name = "-d", aliases = { "--dataType" }, required = true, usage = "Type of input file data")
		public void setDataType(String dataTypeStr) throws CmdLineException {
			this.dataType = ArgsUtils.parseEnum(DataType.class, dataTypeStr);
		}
		public DataType getDataType() {
			return this.dataType;
		}
		
		private File outFile;
		@SuppressWarnings("deprecation")
		@Option(name = "-out", required = true, metaVar = "FILE", usage = "output file")
		public void setOutFile(File f) throws CmdLineException {
			if (f.isDirectory()) {
				throw new CmdLineException("output file is a directory: "+f.getParent());
			}
			if (!f.getParentFile().isDirectory()) {
				throw new CmdLineException("output file directory does not exist: "+f.getParent());
			}
			this.outFile = f;
		}
		public File getOutFile() {
			return outFile;
		}
	}
}
