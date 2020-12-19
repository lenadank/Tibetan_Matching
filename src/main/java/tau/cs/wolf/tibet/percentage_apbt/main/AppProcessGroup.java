package tau.cs.wolf.tibet.percentage_apbt.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kohsuke.args4j.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.data.AppResults;
import tau.cs.wolf.tibet.percentage_apbt.data.Matches;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsMain;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsProcessGroup;
import tau.cs.wolf.tibet.percentage_apbt.misc.DocsCache;
import tau.cs.wolf.tibet.percentage_apbt.misc.UnorderedIntPair;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshaller;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshallerImpl;

public class AppProcessGroup extends AppBase {

	private final static Logger logger = LoggerFactory.getLogger(AppProcessGroup.class);
	
	private final ArgsProcessGroup args;
	
	private final List<Matches> matchesList = new ArrayList<>();
	private final PreprocessDirStructureMarshaller inMarshaller;
	private final DocsCache docsCahce;

	
	public AppProcessGroup(ArgsProcessGroup args) {
		super(args);
		this.args = args;
		this.inMarshaller = new PreprocessDirStructureMarshallerImpl(args.getInDir());
		this.docsCahce = new DocsCache(this.inMarshaller, args.getDocsCacheSize());
	}

	@Override
	public void run() {
		try {
			runWithIOException();
		} catch(IOException e) {
			System.out.println(e);
			throw new IllegalStateException(e);
		}
	}
	
	public List<Matches> getMatchesList() {
		return this.matchesList;
	}
	
	private void runWithIOException() throws IOException {
		int grpId = this.args.getGrpId();
		logger.info("start running on grp ID: " + grpId);
		Set<UnorderedIntPair> grp = this.inMarshaller.readGrp(grpId);
		for (UnorderedIntPair pair: grp) {
			Matches matches = this.runOnDocs(pair.higher, pair.lower);
			this.matchesList.add(matches);
		}
		logger.info("finished running on grp ID: "+grpId);
	}
	
	private Matches runOnDocs(int doc1Id, int doc2Id) throws IOException {
		
		AppMain app = new AppMain(new ArgsMain(args, null, null, null, false), this.props);
		{
			Slicable<?> doc1 = this.docsCahce.get(doc1Id);
			Slicable<?> doc2 = this.docsCahce.get(doc2Id);
			app.setup(doc1, doc2);
		}
		app.run();
		AppResults appResults = app.getResults();
		return new Matches(Integer.toString(doc1Id), Integer.toString(doc2Id), appResults, this.args.getAppStage());
	}

	public static void main(String[] args) throws CmdLineException {
		new AppProcessGroup(new ArgsProcessGroup(args)).run();
	}

}
