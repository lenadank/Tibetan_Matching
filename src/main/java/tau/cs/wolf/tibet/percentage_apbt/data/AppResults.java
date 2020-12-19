package tau.cs.wolf.tibet.percentage_apbt.data;

import java.io.Serializable;
import java.util.List;


public class AppResults implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<MatchResult> apbtMatches;
	private List<MatchResult> unitedMatches;
	
	public void setApbtMatches(List<MatchResult> apbtMatches) {
		this.apbtMatches = apbtMatches;
	}

	public void setUnitedMatches(List<MatchResult> unitedMatches) {
		this.unitedMatches = unitedMatches;
	}

	public void setAlignedMatches(List<MatchResult> alignedMatches) {
		this.alignedMatches = alignedMatches;
	}

	private List<MatchResult> alignedMatches;
	
	public AppResults(List<MatchResult> apbtMatches, List<MatchResult> unitedMatches, List<MatchResult> alignedMatches) {
		super();
		this.apbtMatches = apbtMatches;
		this.unitedMatches = unitedMatches;
		this.alignedMatches = alignedMatches;
	}
	
	public AppResults(List<MatchResult> apbtMatches) {
		this(apbtMatches, null, null);
	}
	
	
	public List<MatchResult> getApbtMatches() {
		return apbtMatches;
	}
	
	public List<MatchResult> getUnitedMatches() {
		return unitedMatches;
	}
	
	public List<MatchResult> getAlignedMatches() {
		return alignedMatches;
	}
	
}
