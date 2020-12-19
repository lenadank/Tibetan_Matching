package tau.cs.wolf.tibet.percentage_apbt.misc;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;

public class LevenshteinDistance {
	public static <R> int computeLevenshteinDistance(Slicable<R> seq1, Slicable<R> seq2) {
		int[][] distance = new int[seq1.length() + 1][seq2.length() + 1];

		for (int i = 0; i <= seq1.length(); i++) {
			distance[i][0] = i;
		}
		for (int j = 1; j <= seq2.length(); j++) {
			distance[0][j] = j;
		}

		for (int i = 1; i <= seq1.length(); i++) {
			for (int j = 1; j <= seq2.length(); j++) {
				distance[i][j] = Utils.min(
						distance[i-1][j] + 1,
						distance[i][j-1] + 1,
						distance[i-1][j-1] + (seq1.compare(i-1, seq2, j-1) ? 0 : 1)
				);
			}
		}
		return distance[seq1.length()][seq2.length()];
	}
}