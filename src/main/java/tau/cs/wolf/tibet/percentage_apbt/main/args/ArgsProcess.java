package tau.cs.wolf.tibet.percentage_apbt.main.args;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

public class ArgsProcess extends ArgsProcessCommon {

	private static final long serialVersionUID = 1L;
	
	public ArgsProcess(String[] args) throws CmdLineException {
		super(args);
	}
	
	private String outDir;
	@SuppressWarnings("deprecation")
	@Option(name = "-o", required = true, metaVar = "DIR", usage = "output dir")
	public void setOutDir(Path dir) throws CmdLineException {
		try {
			Files.createDirectories(dir);
			if (!Files.isDirectory(dir)) {
				throw new CmdLineException("output path is not a directory!!! "+dir);
			}
		} catch (IOException e) {
			throw new CmdLineException(e);
		}
		this.outDir = dir.toString();
	}
	public Path getOutDir() {
		return Paths.get(this.outDir);
	}
	
}
