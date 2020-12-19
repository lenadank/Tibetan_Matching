package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.Serializable;

import org.joda.time.Duration;

public interface Props extends Serializable {
	
	public static final String MIN_LENGTH_PROP_NAME = "minLength";
	public static final String MAX_ERROR_PROP_NAME = "maxError";
	public static final String TIMEOUT_PROP_NAME = "timeout";
	public static final String MIN_DISTANCE_UNION_PROP_NAME = "minDistanceUnion";
	public static final String LOCAL_ALIGN_PAD_RATIO_PROP_NAME = "localAlignPadRatio";
	public static final String MATCH_AWARD_PROP_NAME = "matchAward";
	public static final String MISMATCH_PENALTY_PROP_NAME = "mismatchPenalty";
	public static final String GAP_PENALTY_PROP_NAME = "gapPenalty";
	public static final String CHUNK_SIZE_PROP_NAME = "chunkSize";
	public static final String MAX_MATCH_LENGTH_PROP_NAME = "maxMatchLength";
	public static final String POLL_DURATION_PROP_NAME = "pollDuration";
	public static final String COMPUTE_LEVENSHTEIN_DISTANCE_PROP_NAME = "computeLevenshteinDistance";
	public static final String SYSLOG_CLIENT_PROPS_PATH_PROP_NAME = "syslogClientPropsPath";
	public static final String DOCS_CACHE_SIZE_PROP_NAME = "docsCahceSize";
	
	public static final int MIN_LENGTH_DEFAULT_VALUE = 10;
	public static final int MAX_ERROR_DEFAULT_VALUE = 5;
	public static final Duration TIMEOUT_DEFAULT_VALUE = Duration.standardMinutes(30);
	public static final int MIN_DISTANCE_UNION_DEFAULT_VALUE = 10;
	public static final float LOCAL_ALIGN_PAD_RATIO_DEFAULT_VALUE = 0.12f;
	public static final int MATCH_AWARD_DEFAULT_VALUE = 1;
	public static final int MISMATCH_PENALTY_DEFAULT_VALUE = -1;
	public static final int GAP_PENALTY_DEFAULT_VALUE = -1;
	public static final int CHUNK_SIZE_DEFAULT_VALUE = 1000;
	public static final int MAX_MATCH_LENGTH_DEFAULT_VALUE = 500;
	public static final Duration POLL_DURATION_DEFAULT_VALUE = Duration.standardMinutes(5);
	public static final boolean COMPUTE_LEVENSHTEIN_DISTANCE_DEFAULT_VALUE = true;
	public static final long DOCS_CACHE_SIZE_DEFAULT_VALUE = 1000000000L;
	
	public int getMinLength();
	public int getMaxError();
	public Duration getTimeout();
	public int getMinDistanceUnion();
	public float getLocalAlignPadRatio();
	public int getMismatchPenalty();
	public int getMatchAward();
	public int getGapPenalty();
	public int getChunkSize();
	public int getMaxMatchLength();
	public Duration getPollDuration();
	public boolean getComputeLevenshteinDistance();
	public String getSyslogClientPropsPath();
	public long getDocsCacheSize();
	
}