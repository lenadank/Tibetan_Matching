package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.util.Properties;

import org.joda.time.Duration;
import org.joda.time.Period;

public class PropsImpl implements Props {
	
	private static final long serialVersionUID = 1L;
	
	private int minLength = MIN_LENGTH_DEFAULT_VALUE;
	private int maxError = MAX_ERROR_DEFAULT_VALUE;
	private Duration timeout = TIMEOUT_DEFAULT_VALUE;
	private int minDistanceUnion = MIN_DISTANCE_UNION_DEFAULT_VALUE;
	private float localAlignPadRatio = LOCAL_ALIGN_PAD_RATIO_DEFAULT_VALUE;
	private int matchAward = MATCH_AWARD_DEFAULT_VALUE;
	private int mismatchPenalty = MISMATCH_PENALTY_DEFAULT_VALUE;
	private int gapPenalty = GAP_PENALTY_DEFAULT_VALUE;
	private int chunkSize = CHUNK_SIZE_DEFAULT_VALUE;
	private int maxMatchLength = MAX_MATCH_LENGTH_DEFAULT_VALUE;
	private Duration pollDuration = POLL_DURATION_DEFAULT_VALUE;
	private boolean computeLevenshteinDistance = COMPUTE_LEVENSHTEIN_DISTANCE_DEFAULT_VALUE;
	private String syslogClientPropsPath = null;
	private long docsCacheSize = DOCS_CACHE_SIZE_DEFAULT_VALUE;
	
	@Override public int getMinLength() { return this.minLength; }
	@Override public int getMaxError() { return this.maxError; }
	@Override public Duration getTimeout() { return this.timeout; }
	@Override public int getMinDistanceUnion() { return this.minDistanceUnion; }
	@Override public float getLocalAlignPadRatio() { return this.localAlignPadRatio; }
	@Override public int getMismatchPenalty() { return this.mismatchPenalty; }
	@Override public int getMatchAward() { return this.matchAward; }
	@Override public int getGapPenalty() { return this.gapPenalty; }
	@Override public int getChunkSize() { return this.chunkSize; }
	@Override public int getMaxMatchLength() { return this.maxMatchLength; }
	@Override public Duration getPollDuration() { return this.pollDuration; }
	@Override public boolean getComputeLevenshteinDistance() { return this.computeLevenshteinDistance; }
	@Override public String getSyslogClientPropsPath() { return this.syslogClientPropsPath; }
	@Override public long getDocsCacheSize() { return this.docsCacheSize; }
	
	PropsImpl() {
		this(null);
	}
	PropsImpl(Properties props) {
		if (props != null) {
			
			{
				String val = props.getProperty(MIN_LENGTH_PROP_NAME);
				if (val != null) {
					this.minLength = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(MAX_ERROR_PROP_NAME);
				if (val != null) {
					this.maxError = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(TIMEOUT_PROP_NAME);
				if (val != null) {
					this.timeout = Period.parse(val).toStandardDuration();
				}
			}
			{
				String val = props.getProperty(MIN_DISTANCE_UNION_PROP_NAME);
				if (val != null) {
					this.minDistanceUnion = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(LOCAL_ALIGN_PAD_RATIO_PROP_NAME);
				if (val != null) {
					this.localAlignPadRatio = Float.parseFloat(val);
				}
			}
			{
				String val = props.getProperty(MATCH_AWARD_PROP_NAME);
				if (val != null) {
					this.matchAward = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(MISMATCH_PENALTY_PROP_NAME);
				if (val != null) {
					this.mismatchPenalty = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(GAP_PENALTY_PROP_NAME);
				if (val != null) {
					this.gapPenalty = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(CHUNK_SIZE_PROP_NAME);
				if (val != null) {
					this.chunkSize = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(MAX_MATCH_LENGTH_PROP_NAME);
				if (val != null) {
					this.maxMatchLength = Integer.parseInt(val);
				}
			}
			{
				String val = props.getProperty(POLL_DURATION_PROP_NAME);
				if (val != null) {
					this.pollDuration = Period.parse(val).toStandardDuration();
				}
			}
			{
				String val = props.getProperty(COMPUTE_LEVENSHTEIN_DISTANCE_PROP_NAME);
				if (val != null) {
					this.computeLevenshteinDistance = Boolean.parseBoolean(val);
				}
			}
			{
				String val = props.getProperty(SYSLOG_CLIENT_PROPS_PATH_PROP_NAME);
				if (val != null) {
					this.syslogClientPropsPath = val;
				}
			}
			{
				String val = props.getProperty(DOCS_CACHE_SIZE_PROP_NAME);
				if (val != null) {
					this.docsCacheSize = Long.parseLong(val);
				}
			}
		}
	}
	@Override
	public String toString() {
		return "PropsImpl [minLength=" + minLength + ", maxError=" + maxError + ", timeout=" + timeout
				+ ", minDistanceUnion=" + minDistanceUnion + ", localAlignPadRatio=" + localAlignPadRatio
				+ ", matchAward=" + matchAward + ", mismatchPenalty=" + mismatchPenalty + ", gapPenalty=" + gapPenalty
				+ ", chunkSize=" + chunkSize + ", maxMatchLength=" + maxMatchLength + ", pollDuration=" + pollDuration
				+ ", computeLevenshteinDistance=" + computeLevenshteinDistance + ", syslogClientPropsPath="
				+ syslogClientPropsPath + "]";
	}
	
}