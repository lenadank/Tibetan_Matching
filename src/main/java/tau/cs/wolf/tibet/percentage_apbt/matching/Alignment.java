package tau.cs.wolf.tibet.percentage_apbt.matching;

import java.util.ArrayList;
import java.util.List;

import tau.cs.wolf.tibet.percentage_apbt.data.IndexPair;
import tau.cs.wolf.tibet.percentage_apbt.data.IndexSpan;
import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;
import tau.cs.wolf.tibet.percentage_apbt.misc.BaseModule;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class Alignment<R> extends BaseModule {
	
	public Alignment(Props props, ArgsCommon args) {
		super(props, args);
	}

	public List<MatchResult> alignMatches(List<MatchResult> matches, Slicable<R> seq1, Slicable<R> seq2) {
		List<MatchResult> res = new ArrayList<MatchResult>(matches.size());
		
		for (MatchResult unitedMatch: matches) {
			IndexSpan paddedSpan1 = padSpan(unitedMatch.getInterval().getSpan1());
			IndexSpan paddedSpan2 = padSpan(unitedMatch.getInterval().getSpan2());

			int startOne = Math.max(0, paddedSpan1.getStart());
			int endOne = Math.min(paddedSpan1.getEnd(), seq1.length()-1);
			int startTwo = Math.max(0, paddedSpan2.getStart());
			int endTwo = Math.min(paddedSpan2.getEnd() ,seq2.length()-1);

			MatchResult wateredUnitedMatch = water(seq1.slice(startOne, endOne), seq2.slice(startTwo, endTwo));
			wateredUnitedMatch.getInterval().shiftSpans(startOne, startTwo);
			res.add(wateredUnitedMatch);
		}
		return res;
	}

	public IndexSpan padSpan(IndexSpan span) {
		int spanLen = span.getEnd() - span.getStart() +1;
		int newStart = (int) Math.floor(span.getStart() - spanLen * args.getLocalAlignPadRatio());
		int newEnd = (int) Math.ceil(span.getEnd() + spanLen * args.getLocalAlignPadRatio());
		return new IndexSpan(newStart, newEnd);
	}

	private static enum PathTrace {
		UP, LEFT, DIAGONAL
	}

	public MatchResult water(Slicable<R> seq1, Slicable<R> seq2) {
		// Generate DP table and traceback path pointer matrix

		double[][] scores = new double[seq1.length()+1][seq2.length()+1];  // the DP table               
		PathTrace[][] pointer = new PathTrace[seq1.length()+1][seq2.length()+1];// to store the traceback path

		double maxScore = 0;// initial maximum score in DP table

		int maxI = -1, maxJ = -1;

		// Calculate DP table and mark pointers
		for (int i=1; i<seq1.length()+1; i++) {
			for (int j=1; j<seq2.length()+1; j++) {
				double scoreDiagonal = scores[i-1][j-1] + matchScore(seq1,i-1, seq2, j-1);
//				if (scoreDiagonal > 0.91890628){
//					System.out.println("prevScore: " + scores[i-1][j-1]);
//					System.out.println("currScore: " + matchScore(seq1,i-1, seq2, j-1));
//					int[] seq1Array = (int[])seq1.get();
//					int[] seq2Array = (int[])seq2.get();
//					System.out.println("currMatches: "  + seq1Array[i-1] + " ; " + seq2Array[j-1]);
//				} 
				double scoreUp = scores[i-1][j] + gapPenalty(seq1, i-1);
				double scoreLeft = scores[i][j-1] + gapPenalty(seq2, j-1);
				double score = maxOf4(0,scoreLeft, scoreUp, scoreDiagonal);
				scores[i][j] = score;

				if (score==0) {
					// leave null
				} else if (score==scoreLeft) {
					pointer[i][j] = PathTrace.LEFT;
				} else if (score == scoreUp) {
					pointer[i][j] = PathTrace.UP;
				} else if (score == scoreDiagonal) {
					pointer[i][j] = PathTrace.DIAGONAL;
				} else {
					throw new IllegalStateException("Should not have reached this line");
				}

				if (scores[i][j] >= maxScore) {
					maxI = i;
					maxJ = j;
					maxScore = scores[i][j];
				}
			}
		}

		int i = maxI, j = maxJ;// indices of path starting point

		// traceback, follow pointers
		while (pointer[i][j] != null) {
			switch (pointer[i][j]) {
			case DIAGONAL:
				i--;
				j--;
				break;
			case UP:
				i--;
				break;
			case LEFT:
				j--;
				break;
			default:
				throw new IllegalStateException("Unknown value: "+pointer[i][j]);
			}
		}
		return new MatchResult(Interval.newIntervalByStartEnd(new IndexPair(i, j), new IndexPair(maxI, maxJ)), maxScore);
	}


	private static double maxOf4(double n1, double n2, double n3, double n4) {
		return Math.max(Math.max(n1, n2), Math.max(n3, n4));
	}

	
	public double matchScore(Slicable<R> seq1, int seq1Index, Slicable<R> seq2, int seq2Index ){
		return seq1.compare(seq1Index, seq2, seq2Index)? props.getMatchAward() : props.getMismatchPenalty();
 
	}
	
	public double gapPenalty(Slicable<R> seq1, int i) {
		 return props.getGapPenalty();
	}
}
