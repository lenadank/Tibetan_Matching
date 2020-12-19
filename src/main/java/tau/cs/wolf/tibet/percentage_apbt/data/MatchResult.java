package tau.cs.wolf.tibet.percentage_apbt.data;

import java.util.regex.Pattern;

import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;

public class MatchResult implements Comparable<MatchResult> {

	private final Interval interval;
	private final double score;

	public MatchResult(Interval interval, double score) {
		this.interval = interval;
		this.score = score;
	}
	
	public MatchResult(MatchResult match) {
		this.interval = new Interval(match.getInterval());
		this.score = match.getScore();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchResult other = (MatchResult) obj;
		if (getInterval() == null) {
			if (other.getInterval() != null)
				return false;
		} else if (!getInterval().equals(other.getInterval()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str = getInterval().getStart().getIndex1() + ","
				+ getInterval().getStart().getIndex2() + ","
				+ getInterval().getEnd().getIndex1() + ","
				+ getInterval().getEnd().getIndex2() + "," + getScore();
		return str;
	}

	public static class DefaultFormatter implements Utils.Formatter<MatchResult> {
		@Override
		public String format(MatchResult match) {
			StringBuilder sb = new StringBuilder();
			sb.append(match.getInterval().getStart().getIndex1());
			sb.append(",");
			sb.append(match.getInterval().getStart().getIndex2());
			sb.append(",");
			sb.append(match.getInterval().getEnd().getIndex1());
			sb.append(",");
			sb.append(match.getInterval().getEnd().getIndex2());
			sb.append(",");
			sb.append(match.getScore());

			return sb.toString();
		}
	}
	
	private static final Pattern defaultParserDelimiter = Pattern.compile("\\s*,\\s*");
	
	public static class DefaultParser implements Utils.Parser<MatchResult> {

		@Override
		public MatchResult parse(String str) throws IllegalArgumentException {
			String[] splitStr = defaultParserDelimiter.split(str, 6);
			if (splitStr.length != 5) {
				throw new IllegalArgumentException("each line must have exactly 5 comma delimited fields: "+str);
			}
			int[] indices = new int[4];
			for (int i=0; i<4; i++) {
				try {
					indices[i] = Integer.parseInt(splitStr[i]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("field with index "+i+" is not an Integer", e);
				}
			}
			double score;
			try {
				score = Double.parseDouble(splitStr[4]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("field with index "+4+" is not a Double", e);
			}
			return new MatchResult(Interval.newIntervalByStartEnd(new IndexPair(indices[0], indices[1]), new IndexPair(indices[2], indices[3])), score);
		}
		
	}

	@Override
	public int compareTo(MatchResult other) {
		return this.getInterval().compareTo(other.getInterval());
	}

	public Interval getInterval() {
		return interval;
	}

	public double getScore() {
		return score;
	}

}
