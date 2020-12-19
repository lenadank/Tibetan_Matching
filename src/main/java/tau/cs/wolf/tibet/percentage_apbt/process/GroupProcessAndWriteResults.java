package tau.cs.wolf.tibet.percentage_apbt.process;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.main.AppProcessGroup;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsProcessGroup;

public class GroupProcessAndWriteResults extends AppProcessGroup {

	private static Logger logger = LoggerFactory.getLogger(GroupProcessAndWriteResults.class);
	private final ProcessDirStructureMarshaller outMarshaller;
	private final int grpId;
	
	public GroupProcessAndWriteResults(ArgsProcessGroup args, ProcessDirStructureMarshaller outMarshaller) {
		super(args);
		this.outMarshaller = outMarshaller;
		this.grpId = args.getGrpId();
	}
	
	@Override
	public void run() {
		super.run();
		try {
			this.outMarshaller.writeGroupResults(this.grpId, super.getMatchesList());
			logger.info("finished writing results for grp ID: "+this.grpId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}