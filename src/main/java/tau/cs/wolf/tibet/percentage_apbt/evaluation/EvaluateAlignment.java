package tau.cs.wolf.tibet.percentage_apbt.evaluation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class EvaluateAlignment {
	
	private List<Match> alignments =  new ArrayList<>();
	private List<Match> correctAlignment = new ArrayList<>();
	
	public static void main(String[] args) throws Exception{
		EvaluateAlignment eA = new EvaluateAlignment();
		eA.analyzedResults("C:/Users/lenadank/Documents/GitHub/Tibet/test5/correct_alignment_spans.csv", "C:/data/Results/Tibet/stem_only/test_tf_idf_ranking_new.union.sorted.txt");
	}
	
	public void analyzedResults(String correctAlignmentFileName , String resultsFileName ) throws Exception{
		this.loadAlignmentFile(resultsFileName, alignments);
		this.loadAlignmentFile(correctAlignmentFileName, correctAlignment);
		int i = 0;
		int correct = 0;
		int incorrect = 0;
		int average_match_length_1 = 0;
		int average_match_length_2 = 0;
		Collections.sort(alignments);
		Set<Match> coveredMatches = new HashSet<>();
		List<Integer> incorrectIndices = new ArrayList<>();
		for (Match m : alignments){
			average_match_length_1 += m.span1.getEnd()-m.span1.getStart()+1;
			average_match_length_2 += m.span2.getEnd()-m.span2.getStart()+1;
			boolean isCorrect = false;
			i++;
			if (isCorrectMatch(m, coveredMatches)){
				correct += 1;
				isCorrect = true;
			}
			else{
				incorrect +=1;
				incorrectIndices.add(i);
			}
			if (i < 50 && (i%5 == 0)){
				System.out.println("p@" + i + " = " + (double)correct/(correct+incorrect) +" " + correct + " " + (correct+incorrect) );
				//System.out.println((i-1) + " : " + m + " : " + isCorrect);
			}
			//print oncovered matches:
		}
		System.out.println("recall = " + (double)coveredMatches.size()/correctAlignment.size());
		System.out.println("number of detected matched " + alignments.size());
		System.out.println("average match length = " + ((double)average_match_length_1)/alignments.size() + " ; " + ((double)average_match_length_2)/alignments.size());
		int cnt = 1;
		System.out.println("incorrectIndices: " + incorrectIndices.subList(0, 20));
		System.out.println("unmatched:");
		for (Match correctMatch : correctAlignment){
			if (!coveredMatches.contains(correctMatch)){
				System.out.println("\t"+ cnt++ + " " + correctMatch);
			}
		}


	}
	
	//check for overlap
	private boolean isCorrectMatch(Match match, Set<Match> matches){
		boolean matched = false;
		for (Match correctMatch : correctAlignment){
			if (match.intersects(correctMatch)){
					matched = true;
					matches.add(correctMatch);
			}
		}
		return matched;
	}
	
	private void loadAlignmentFile(String file, List<Match> matches) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
		for (String line : lines){
			String[] words = line.split(",");
			Span span1 = new Span(Integer.parseInt(words[0]), Integer.parseInt(words[2]));
			Span span2 = new Span(Integer.parseInt(words[1]), Integer.parseInt(words[3]));
			matches.add(new Match(span1, span2, Double.parseDouble(words[4])));
		}
	}
	
	
	static class Match implements Comparable<Match>{
		Span span1;
		Span span2;
		double score;
		public Match(Span span1, Span span2, double score) {
			super();
			this.span1 = span1;
			this.span2 = span2;
			this.score = score;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(score);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((span1 == null) ? 0 : span1.hashCode());
			result = prime * result + ((span2 == null) ? 0 : span2.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Match other = (Match) obj;
			if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
				return false;
			if (span1 == null) {
				if (other.span1 != null)
					return false;
			} else if (!span1.equals(other.span1))
				return false;
			if (span2 == null) {
				if (other.span2 != null)
					return false;
			} else if (!span2.equals(other.span2))
				return false;
			return true;
		}
		@Override
		public int compareTo(Match o) {
			return Double.compare(o.score, this.score);
		}
		
		public boolean intersects(Match other){
			return this.span1.intersects(other.span1) && this.span2.intersects(other.span2);
		}
		@Override
		public String toString(){
			return "(" + span1 + " , " + span2 + ")";
		}
		
	}
	
	
	static class Span{
		int start;
		int end;

		public Span(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}
		
		
		public int getStart() {
			return start;
		}


		public int getEnd() {
			return end;
		}


		public boolean intersects(Span s){
			return !(s.getStart() > this.getEnd()) && !(s.getEnd() < this.getStart());
		}


		@Override
		public String toString() {
			return "[" + start + ", " + end + "]";
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + end;
			result = prime * result + start;
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Span other = (Span) obj;
			if (end != other.end)
				return false;
			if (start != other.start)
				return false;
			return true;
		}
	
		

	}
	
}
