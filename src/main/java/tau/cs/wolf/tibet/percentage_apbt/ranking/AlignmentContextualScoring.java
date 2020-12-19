package tau.cs.wolf.tibet.percentage_apbt.ranking;


public interface AlignmentContextualScoring {

	double matchScore(int... token);
	double replaceScore(int token1, int token2);
	double gapScore(int token);
}
