package tau.cs.wolf.tibet.percentage_apbt.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsBase;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;
import tau.cs.wolf.tibet.percentage_apbt.matching.Apbt;
import tau.cs.wolf.tibet.percentage_apbt.matching.ProcessType;
import tau.cs.wolf.tibet.percentage_apbt.misc.BaseModule;
import tau.cs.wolf.tibet.percentage_apbt.misc.LevenshteinDistance;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;

@SuppressWarnings("all")
public class WorkerThread<R extends java.lang.reflect.Array> extends BaseModule implements Runnable {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final Slicable<R> seq1;
	private final Slicable<R> seq2;
	private final Interval workInterval;
	private final MatchesContainer rContainer;
	private final int threadId;

	public WorkerThread(Props props, ArgsCommon args, Slicable<R> seq1, Slicable<R> seq2, Interval workInterval, MatchesContainer rContainer, int threadId) {
		super(props, args);
		this.workInterval = workInterval;
		this.seq1 = seq1;
		this.seq2 = seq2;
		this.rContainer = rContainer;
		this.threadId = threadId;
	}


	public void run() {
		long startTime = System.currentTimeMillis();
		logger.info("Thread: "+this.threadId+". Started with work interval: "+this.workInterval);

		Apbt<? super R> apbt = new Apbt<R>();
		ProcessType type = seq1.length()<seq2.length() ? ProcessType.ROW : ProcessType.COL;
		apbt.setup(seq1, seq2, type, args, props);
		apbt.run();
		Utils.reportComputationTimeByStartTime(logger, startTime, "Thread: "+this.threadId+". Finished processing");
		
		for(Interval curSolution: apbt.getSolutions()) {
			Slicable<R> seq1Slice = seq1.slice(curSolution.getStart().getIndex1(),curSolution.getEnd().getIndex1()+1);
			Slicable<R> seq2Slice = seq2.slice(curSolution.getStart().getIndex2(),curSolution.getEnd().getIndex2()+1);
			double score = props.getComputeLevenshteinDistance() ? LevenshteinDistance.computeLevenshteinDistance(seq1Slice, seq2Slice) : -1;
			curSolution.shiftSpans(workInterval.getStart().getIndex1(), workInterval.getStart().getIndex2());
			MatchResult newResult = new MatchResult(curSolution, score);
			rContainer.addResult(newResult);
		}
		Utils.reportComputationTimeByStartTime(logger, startTime, "Thread: "+this.threadId+". Finished writing results");
	}



}
