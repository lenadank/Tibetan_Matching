package tau.cs.wolf.tibet.percentage_apbt.main;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.kohsuke.args4j.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.concurrent.MatchesContainer;
import tau.cs.wolf.tibet.percentage_apbt.concurrent.WorkerThread;
import tau.cs.wolf.tibet.percentage_apbt.data.AppResults;
import tau.cs.wolf.tibet.percentage_apbt.data.IndexPair;
import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParser;
import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.SrcType;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsMain;
import tau.cs.wolf.tibet.percentage_apbt.matching.Alignment;
import tau.cs.wolf.tibet.percentage_apbt.matching.Union;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;
import tau.cs.wolf.tibet.percentage_apbt.ranking.IDFScoring;
import tau.cs.wolf.tibet.percentage_apbt.ranking.IntRankingAlignment;

public class AppMain extends AppCommon {

	private static final Logger logger = LoggerFactory.getLogger(AppMain.class);
	
	public AppMain(ArgsMain args, Props props) {
		super(args, props);
	}
	
	private Slicable<?> seq1 = null;
	private Slicable<?> seq2 = null;
	private long startTime = System.currentTimeMillis(); 
	
	public void setup(Slicable<?> seq1, Slicable<?> seq2) {
		this.seq1 = seq1;
		this.seq2 = seq2;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public AppResults calcResults() {
		if (this.seq1 == null) {
			SlicableParser<?, File> parser = (SlicableParser<?, File>) AppUtils.getParser(args.getDataType(), SrcType.FILE);
			seq1 = parser.parse(args.getInFile1());
		}
		if (this.seq2 == null) {
			SlicableParser<?, File> parser = (SlicableParser<?, File>) AppUtils.getParser(args.getDataType(), SrcType.FILE);
			seq2 = parser.parse(args.getInFile2());
		}
		
		startTime = System.currentTimeMillis();
		
		MatchesContainer matchesContainer = new MatchesContainer(new MatchResult.DefaultFormatter(), -1);
		Interval interval = Interval.newIntervalByStartEnd(new IndexPair(0, 0), new IndexPair(seq1.length(), seq2.length()));
		new WorkerThread(props, args, seq1, seq2, interval, matchesContainer, 0).run();
		matchesContainer.shutdown();
		switch (args.getAppStage()) {
			case APBT: {
				return doApbt(matchesContainer);
			}
			case UNION: {
				return doUnion(matchesContainer);
			}
			case ALIGNMENT: {
				return doAlignment(matchesContainer);
			}
			default: {
				throw new IllegalArgumentException("Unknown value: "+args.getAppStage());
			}
		}
	}
	
	@Override
	protected void _writeResults(AppResults results) {
		String outFileBase = FilenameUtils.removeExtension(args.getOutFile().getPath());
		switch (args.getAppStage()) {
			case APBT: {
				Utils.writeMatches(this.args.getOutFile(), results.getApbtMatches(), null);
				break;
			}
			case UNION: {
				Utils.writeMatches(new File(outFileBase + ".apbt.txt"), results.getApbtMatches(), null);
				Utils.writeMatches(this.args.getOutFile(), results.getUnitedMatches(), null);
				break;
			}
			case ALIGNMENT: {
				System.out.println("writing results to " + outFileBase);
				Utils.writeMatches(new File(outFileBase + ".apbt.txt"), results.getApbtMatches(), null);
				Utils.writeMatches(new File(outFileBase + ".union.txt"), results.getUnitedMatches(), null);
				Utils.writeMatches(this.args.getOutFile(), results.getAlignedMatches(), null);
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown value: "+args.getAppStage());
			}
		}
	}

	private AppResults doApbt(MatchesContainer matchesContainer) {
		List<MatchResult> apbtMatches = matchesContainer.getResults();
		Utils.reportComputationTimeByStartTime(logger, startTime, "Finished APBT stage with "+apbtMatches.size()+" matches");
		return new AppResults(apbtMatches, null, null);
	}
	
	private AppResults doUnion(MatchesContainer matchesContainer) {
		AppResults res = doApbt(matchesContainer);
		List<MatchResult> unitedMatches = new Union(props, args).uniteMatches(res.getApbtMatches());
		Utils.reportComputationTimeByStartTime(logger, startTime, "Finished UNION stage with "+unitedMatches.size()+" matches");
		res.setUnitedMatches(unitedMatches);
		return res;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private AppResults doAlignment(MatchesContainer matchesContainer) {
		//System.out.println("running alignment");
		AppResults res = doUnion(matchesContainer);
		List<MatchResult> alignedMatches;
		switch (args.getDataType()) {
		case INT:
			alignedMatches = new IntRankingAlignment(props, args,new IDFScoring()).alignMatches(res.getUnitedMatches(), (Slicable<int[]>) seq1, (Slicable<int[]>) seq2);
			break;
		case CHAR:
			alignedMatches = new Alignment(props, args).alignMatches(res.getUnitedMatches(), seq1, seq2);
			break;
		default:
			throw new IllegalArgumentException("Unknown value: "+args.getDataType());
		}
		Utils.reportComputationTimeByStartTime(logger, startTime, "Finished ALIGNMENT stage with "+alignedMatches.size()+" matches");
		res.setAlignedMatches(alignedMatches);
		return res;
	}
	
	public static void main(String[] args) {
		try {
			AppMain app = new AppMain(new ArgsMain(args), null);
			app.run();
			app.writeResults();
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}


}
