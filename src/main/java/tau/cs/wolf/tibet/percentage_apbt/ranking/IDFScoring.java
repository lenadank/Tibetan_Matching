
package tau.cs.wolf.tibet.percentage_apbt.ranking;

import tau.cs.wolf.tibet.percentage_apbt.ranking.utils.stemming.StemUtils;
import tau.cs.wolf.tibet.percentage_apbt.ranking.utils.tfidf.TermFrequencyForCorpus;

public class IDFScoring implements AlignmentContextualScoring {
	private static final int MIN_DOC_FREQUENCY_TH = 3;

	
	private double getTfIdfScore(int... token){
		int df = TermFrequencyForCorpus.getDFForStem(token);
		if (df >= MIN_DOC_FREQUENCY_TH){
			return (TermFrequencyForCorpus.DOC_NUM_IN_CORPUS - df + 3)/(double)(TermFrequencyForCorpus.DOC_NUM_IN_CORPUS- MIN_DOC_FREQUENCY_TH);
		}
		else{
			// if the word is not frequent enough, we treat it as an insignificant word, that is, a very frequent one.
			return 0;
		}

	}
	@Override
	public double matchScore(int... token) {
		return (getTfIdfScore(token));
	//	return 1;
	}

	@Override
	public double replaceScore(int token1, int token2) {
		try{
			return getLCSBetweenTwoStems(token1, token2);
		}
		catch(Exception e){
			return -1;
		}
		//return -1;
	}

	@Override
	public double gapScore(int token) {
//		if (token == -1){
//			return -1; //
//		}
//		return -1;
		return -getTfIdfScore(token);
	}
	
	//consider using the vector representation and calculate the distance using the weights for each slot
	private double getEditDistanceBetweenTwoStems(int token1, int token2){
		String stem1 = StemUtils.getStemForInt(token1);
		String stem2 = StemUtils.getStemForInt(token2);
		return getLevinshteinDistance(stem1, stem2);
	}
	
	private double getLCSBetweenTwoStems(int token1, int token2){
		String stem1 = StemUtils.getStemForInt(token1);
		String stem2 = StemUtils.getStemForInt(token2);
		int lcs = getLCS(stem1, stem2);
		if (lcs > Math.max(stem1.length(), stem2.length())*0.5){ // if most of the letters are shared
			return lcs/(double)Math.max(stem1.length(), stem2.length()) * Math.max(matchScore(token1), matchScore(token2));
		}
		else{ //most of the letters are different
			return (gapScore(token1)+ gapScore(token2));
		}
	}

	
	
	private int getLCS(String stem1, String stem2){
		char[] stem1Chars = stem1.toCharArray();
		char[] stem2Chars = stem2.toCharArray();
		int[][] editDistanceMatrix = new int[stem1.length()+1][stem2.length()+1];
		for (int i = 0; i <= stem1.length(); i++)
			editDistanceMatrix[i][0] = 0;
		for (int j = 1; j <= stem2.length(); j++)
			editDistanceMatrix[0][j] = 0;
		for (int i = 1; i <= stem1.length(); i++) {

			for (int j = 1; j <= stem2.length(); j++) {

				editDistanceMatrix[i][j] = Math.max(
						editDistanceMatrix[i - 1][j] ,
						Math.max(editDistanceMatrix[i][j - 1],
						editDistanceMatrix[i - 1][j - 1]
								+ (stem1Chars[i-1] == stem2Chars[j-1] ? 1 : 0)));
			}
		}
		return editDistanceMatrix[stem1.length()][stem2.length()];


	}
	
	private double getLevinshteinDistance(String stem1, String stem2){
		char[] stem1Chars = stem1.toCharArray();
		char[] stem2Chars = stem2.toCharArray();

		int[][] editDistanceMatrix = new int[stem1.length()+1][stem2.length()+1];
		for (int i = 0; i <= stem1.length(); i++)
			editDistanceMatrix[i][0] = i;
		for (int j = 1; j <= stem2.length(); j++)
			editDistanceMatrix[0][j] = j;

		for (int i = 1; i <= stem1.length(); i++) {

			for (int j = 1; j <= stem2.length(); j++) {

				editDistanceMatrix[i][j] = Math.min(
						editDistanceMatrix[i - 1][j] +1,
						Math.min(editDistanceMatrix[i][j - 1] + 1,
						editDistanceMatrix[i - 1][j - 1]
								+ (stem1Chars[i-1] == stem2Chars[j-1] ? 0 : 1)));
			}
		}
		return editDistanceMatrix[stem1.length()][stem2.length()];

	}
	public static void main(String[] args){
		System.out.println(new IDFScoring().getLevinshteinDistance("abcd", "ab"));
		System.out.println(new IDFScoring().getLCS("abcd", "abeed"));
		System.out.println(new IDFScoring().gapScore(1));
	}

}
