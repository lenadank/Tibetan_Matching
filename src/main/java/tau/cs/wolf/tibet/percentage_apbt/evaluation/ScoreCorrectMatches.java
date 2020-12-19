package tau.cs.wolf.tibet.percentage_apbt.evaluation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import tau.cs.wolf.tibet.percentage_apbt.data.IndexSpan;
import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserIntFile;
import tau.cs.wolf.tibet.percentage_apbt.main.args.Args;
import tau.cs.wolf.tibet.percentage_apbt.misc.PropsBuilder;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;
import tau.cs.wolf.tibet.percentage_apbt.ranking.IDFScoring;
import tau.cs.wolf.tibet.percentage_apbt.ranking.IntRankingAlignment;

import com.google.common.io.Files;


public class ScoreCorrectMatches {
	public static final String textInputFolder = "C:/Users/lenadank/Documents/GitHub/Tibet/test5/";
	public static final String file1Name = textInputFolder + "/enum_stem/Klong.txt";
	public static final String file2Name = textInputFolder + "/enum_stem/phya.txt";
	public static final String correctMatchesFile = /*textInputFolder +*/ "C:/data/Results/Tibet/int/test5_stem.union.txt"/* "correct_alignment_spans.csv"*/;
	public static final String output = /*textInputFolder + */"C:/data/Results/Tibet/stem/test5_stem.alignment.txt";//*"correct_alignment_spans.scored.csv"*/;

	public static void main(String[] argsToMain) throws Exception{
		ScoreCorrectMatches sCM = new ScoreCorrectMatches();
		List<MatchResult> matchResult = sCM.loadUnitedMatches();
		SlicableParserIntFile sPIF = new SlicableParserIntFile();
		Slicable<int[]> seq1 = sPIF.parse(new File(file1Name));
		Slicable<int[]> seq2 = sPIF.parse(new File(file2Name));
		Args args = new Args(argsToMain);
		args.fillWithProps(PropsBuilder.defaultProps());

		List<MatchResult> res = new IntRankingAlignment(null, args,new IDFScoring()).alignMatches(matchResult, (Slicable<int[]>) seq1, (Slicable<int[]>) seq2);

		//List<MatchResult> res = new IntRankingAlignment(null, args,new IDFScoring()).alignMatches(matchResult, (Slicable<int[]>) seq1, (Slicable<int[]>) seq2);
		Utils.writeMatches(new File(output), res, null);
		System.out.println("results written to: " + output);
	}
	private List<MatchResult> loadUnitedMatches() throws IOException{
		List<MatchResult> matchedResults = new ArrayList<>();
		List<String> lines = Files.readLines(new File(correctMatchesFile), Charset.defaultCharset());
		for (String line: lines){
			String[] words = line.split(",");
			Interval interval = Interval.newIntervalBySpans(new IndexSpan(Integer.parseInt(words[0]), Integer.parseInt(words[2])), 
					new IndexSpan(Integer.parseInt(words[1]), Integer.parseInt(words[3])));
			MatchResult mRes = new MatchResult(interval, Double.parseDouble(words[4]));
			matchedResults.add(mRes);
		}
		return matchedResults;
	}
}
