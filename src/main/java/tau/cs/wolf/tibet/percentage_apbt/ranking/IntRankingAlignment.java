package tau.cs.wolf.tibet.percentage_apbt.ranking;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;
import tau.cs.wolf.tibet.percentage_apbt.matching.Alignment;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class IntRankingAlignment extends Alignment<int[]> {

	AlignmentContextualScoring mScoring;
	
	public IntRankingAlignment(Props props, ArgsCommon args, AlignmentContextualScoring scoring) {
		super(props, args);
		mScoring = scoring;
	}
	
	@Override
	public double matchScore(Slicable<int[]> seq1, int seq1Index, Slicable<int[]> seq2, int seq2Index ){
		int token1 = seq1.get()[seq1.getRealIndex(seq1Index)];
		int token2 = seq2.get()[seq2.getRealIndex(seq2Index)];
		if (token1 == token2){
			if (seq1Index+1 < seq1.length() && seq2Index +1  < seq2.length() && seq1.get()[seq1.getRealIndex(seq1Index+1)] == seq2.get()[seq2.getRealIndex(seq2Index+1)]){
				if (seq1Index+2 < seq1.length() && seq2Index + 2 < seq2.length() && seq1.get()[seq1.getRealIndex(seq1Index+2)] == seq2.get()[seq2.getRealIndex(seq2Index+2)]){
					return mScoring.matchScore(token1, seq1.get()[seq1.getRealIndex(seq1Index+1)], seq1.get()[seq1.getRealIndex(seq1Index+1)]);
				}
				else{
					return mScoring.matchScore(token1, seq1.get()[seq1.getRealIndex(seq1Index+1)]);
				}
			}
			else{
				return mScoring.matchScore(token1);
			}

		}
		return mScoring.replaceScore(token1, token2);
		
 
	}
	
	
	@Override
	public double gapPenalty(Slicable<int[]> seq, int i) {
		 return mScoring.gapScore(seq.get()[i]);
	}

}
