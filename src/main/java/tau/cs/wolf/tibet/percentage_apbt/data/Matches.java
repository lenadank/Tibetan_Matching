package tau.cs.wolf.tibet.percentage_apbt.data;

import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.AppStage;

public class Matches {
	
	private final String p1;
	private final String p2;
	private final AppResults appResults;
	private final AppStage appStage;
		
	public Matches(String p1, String p2, AppResults appResults, AppStage appStage) {
		this.p1 = p1;
		this.p2 = p2;
		this.appResults = appResults;
		this.appStage = appStage;
	}

	@Override
	public String toString() {
		String in1 = this.p1.substring(this.p1.lastIndexOf('/')+1);
		String in2 = this.p2.substring(this.p2.lastIndexOf('/')+1);
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s\t", in1, in2));
		if (this.appStage.order() >= AppStage.APBT.order()) {
			sb.append("APBT:\t");
			for (MatchResult match: appResults.getApbtMatches()) {
				sb.append(match);
				sb.append("\t");
			}
		}
		if (this.appStage.order() >= AppStage.UNION.order()) {
			sb.append("UNION:\t");
			for (MatchResult match: appResults.getUnitedMatches()) {
				sb.append(match);
				sb.append("\t");
			}
		}
		if (this.appStage.order() >= AppStage.ALIGNMENT.order()) {
			sb.append("ALIGNED:\t");
			for (MatchResult match: appResults.getAlignedMatches()) {
				sb.append(match);
				sb.append("\t");
			}
		}
		return sb.toString();
	}

}
