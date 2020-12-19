package tau.cs.wolf.tibet.percentage_apbt.process;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import tau.cs.wolf.tibet.percentage_apbt.data.Matches;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshallerImpl;

public class ProcessDirStructureMarshallerImpl implements ProcessDirStructureMarshaller {

	private final Path grpsResultsDirPath;
	
	public ProcessDirStructureMarshallerImpl(Path basePath) {
		this.grpsResultsDirPath = basePath.resolve("groups");
	}

	@Override
	public boolean isFinishedGrp(int grpId) throws IOException {
		return Files.exists(getGrpFinish(grpId));
	}
	
	@Override
	public FileLock tryLockGrp(int grpId) throws IOException {
		Path grpDir = getGrpDir(grpId);
		Path lockPath = grpDir.resolve("_lock");
		if (!Files.exists(lockPath)) {
			try {
				Files.createFile(lockPath);
			} catch(FileAlreadyExistsException e) {
				// do nothing
			}
		}
		FileChannel channel = FileChannel.open(lockPath, StandardOpenOption.WRITE);
		return channel.tryLock();
	}
	
	@Override
	public void writeGroupResults(int grpId, List<Matches> matchesList) throws IOException {
		Path grpDir = getGrpDir(grpId);
		Path resultsPath = grpDir.resolve("results.txt");
		Files.createFile(resultsPath);
		List<String> matchesStrList = new ArrayList<>(matchesList.size());
		for (Matches matches: matchesList) {
			matchesStrList.add(matches.toString());
		}
		Files.write(resultsPath, matchesStrList);
		Files.createFile(this.getGrpFinish(grpId));
		
	}
	
	private Path getGrpDir(int grpId) throws IOException {
		Path grpPath = this.grpsResultsDirPath.resolve(formatIdDir(grpId));
		Files.createDirectories(grpPath);
		return grpPath;
	}
	
	private Path getGrpFinish(int grpId) throws IOException {
		return getGrpDir(grpId).resolve("_finish");
	}
	
	public static Path formatIdDir(int grpId) {
		return PreprocessDirStructureMarshallerImpl.formatId(grpId, "");
	}


}
