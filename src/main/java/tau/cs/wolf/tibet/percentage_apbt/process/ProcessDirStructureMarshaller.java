package tau.cs.wolf.tibet.percentage_apbt.process;

import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.List;

import tau.cs.wolf.tibet.percentage_apbt.data.Matches;


public interface ProcessDirStructureMarshaller {
	
	boolean isFinishedGrp(int grpId) throws IOException;
	
	public FileLock tryLockGrp(int grpId) throws IOException;

	void writeGroupResults(int grpId, List<Matches> matchesList) throws IOException;
	
}