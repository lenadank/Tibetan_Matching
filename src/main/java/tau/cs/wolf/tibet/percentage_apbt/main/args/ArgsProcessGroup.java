package tau.cs.wolf.tibet.percentage_apbt.main.args;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

public class ArgsProcessGroup extends ArgsProcessCommon {
	
	public ArgsProcessGroup(String[] args) throws CmdLineException {
		super(args);
	}
	
	public ArgsProcessGroup(ArgsProcessCommon argsProcessCommon, int grpId) {
		super(argsProcessCommon);
		this.grpId = grpId;
	}
	
	private static final long serialVersionUID = 1L;
	
	
	@Option(name = "-grpId", required = true, metaVar = "INT", usage = "group ID to process")
	private int grpId;
	public int getGrpId() {
		return this.grpId;
	}
}
