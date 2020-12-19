package tau.cs.wolf.tibet.percentage_apbt.main.args;

import java.nio.file.Path;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class ArgsProcessCommon extends ArgsCommon {

	private static final long serialVersionUID = 1L;
	
	public ArgsProcessCommon(String[] args) throws CmdLineException {
		super(args);
	}
	
	public ArgsProcessCommon(ArgsCommon argsCommon, Path inDir, long docsCacheSize) {
		super(argsCommon);
		this.inDir = inDir;
		this.docsCacheSize = docsCacheSize;
	}
	
	public ArgsProcessCommon(ArgsProcessCommon other) {
		this(other, other.getInDir(), other.getDocsCacheSize());
	}
	
	private Path inDir;
	@Option(name = "-inDir", required = true, metaVar = "DIR", usage = "input dir")
	public void setInDir(Path dir) throws CmdLineException {
		ArgsUtils.assertDirExists(dir, "-d");
		this.inDir = dir;
	}
	
	
	private boolean processInParallel;
	@Option(name = "-parallelProc", required = false, metaVar = "PAR_PROC", usage = "process in parallel")
	public void setProcessInParallel(String processInParallel) throws CmdLineException {
		this.processInParallel = processInParallel == "true";
	}
	
	
	
	public Path getInDir() {
		return this.inDir;
	}

	public boolean getProcessInParallel(){
		return this.processInParallel;
	}
	
	private Long docsCacheSize;
	@SuppressWarnings("deprecation")
	@Option(name = "-cacheSize", metaVar = "LONG", usage = "docs cache size")
	public void setDocsCacheSize(long cacheSize) throws CmdLineException {
		if (cacheSize < 0) {
			throw new CmdLineException("cache size must be positive!!!");
		}
		this.docsCacheSize = cacheSize;
	}
	
	public Long getDocsCacheSize() {
		return this.docsCacheSize;
	}

	@Override
	public void fillWithProps(Props props) throws CmdLineException {
		super.fillWithProps(props);
		if (this.getDocsCacheSize() == null) {
			this.setDocsCacheSize(props.getDocsCacheSize());
		}
	}
}
