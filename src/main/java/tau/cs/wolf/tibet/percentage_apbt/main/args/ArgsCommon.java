package tau.cs.wolf.tibet.percentage_apbt.main.args;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.AppStage;
import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.DataType;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class ArgsCommon extends ArgsBase {

	private static final long serialVersionUID = 1L;
	
	public ArgsCommon(String[] args) throws CmdLineException {
		super(args);
	}
	
	public ArgsCommon(ArgsCommon other) {
		this.appStage = other.appStage;
		this.dataType = other.dataType;
		this.localAlignPadRatio = other.localAlignPadRatio;
		this.maxError = other.maxError;
		this.minDistanceUnion = other.minDistanceUnion;
		this.minLength = other.minLength;
		this.timeout = other.timeout;
	}

	public ArgsCommon(AppStage appStage, DataType dataType) {
		super();
		this.appStage = appStage;
		this.dataType = dataType;
	}

	private Integer minLength = null;
	@Option(name = "-minLength", metaVar = "int", usage = "minimin length for matches")
	public void setMinlength(int minLength) throws CmdLineException {
		ArgsUtils.assertIsNonNegative(minLength, "-minLength");
		this.minLength = minLength;
	}
	public Integer getMinLength() {
		return minLength;
	}

	private Integer maxError = null;
	@Option(name = "-maxError", metaVar = "int", usage = "maximum allowed Levenshtein distance")
	public void setMaxError(Integer maxError) throws CmdLineException {
		ArgsUtils.assertIsNonNegative(maxError, "-maxError");
		this.maxError = maxError;
	}
	public Integer getMaxError() {
		return maxError;
	}

	private Duration timeout = null;
	@Option(name = "-timeout", metaVar = "int", usage = "timeout for execution")
	public void setTimeout(String timeoutStr) throws CmdLineException {
		this.timeout = Period.parse(timeoutStr).toStandardDuration();
	}
	public void setTimeout(Duration duration) {
		this.timeout = Duration.ZERO.plus(duration);
	}
	public Duration getTimeout() {
		return timeout;
	}

	private Integer minDistanceUnion = null;
	@Option(name = "-minDistanceUnion", metaVar = "int", usage = "min distance for union")
	public void setMinDistanceUnion(Integer minDistanceUnion) throws CmdLineException {
		ArgsUtils.assertIsNonNegative(minDistanceUnion, "-minDistanceUnion");
		this.minDistanceUnion = minDistanceUnion;
	}
	public Integer getMinDistanceUnion() {
		return minDistanceUnion;
	}

	private Float localAlignPadRatio = null;
	@Option(name = "-localAlignPadRatio", metaVar = "float", usage = "ratio for local align pad")
	public void setLocalAlignPadRatio(Float localAlignPadRatio) throws CmdLineException {
		ArgsUtils.assertIsFraction(localAlignPadRatio, "-localAlignPadRatio");
		this.localAlignPadRatio = localAlignPadRatio;
	}
	public Float getLocalAlignPadRatio() {
		return this.localAlignPadRatio;
	}

	private DataType dataType;
	@Option(name = "-d", aliases = { "--dataType" }, required = true, usage = "Type of input file data")
	public void setDataType(String dataTypeStr) throws CmdLineException {
		this.dataType = ArgsUtils.parseEnum(DataType.class, dataTypeStr);
	}
	public DataType getDataType() {
		return this.dataType;
	}

	private AppStage appStage;
	@Option(name = "-s", aliases = { "--appStage" }, required = true, usage = "Stage of algorithm to execute")
	public void setAppStage(String appStageStr) throws CmdLineException {
		this.appStage = ArgsUtils.parseEnum(AppStage.class, appStageStr);
	}
	public AppStage getAppStage() {
		return this.appStage;
	}
	
	public void fillWithProps(Props props) throws CmdLineException {
		super.fillWithProps(props);
		
		if (this.getMinLength() == null) {
			this.setMinlength(props.getMinLength());
		}
		if (this.getMaxError() == null) {
			this.setMaxError(props.getMaxError());
		}
		if (this.getTimeout() == null) {
			Duration t = props.getTimeout();
			this.setTimeout(t);
		}
		if (this.getMinDistanceUnion() == null) {
			this.setMinDistanceUnion(props.getMinDistanceUnion());
		}
		if (this.getLocalAlignPadRatio() == null) {
			this.setLocalAlignPadRatio(props.getLocalAlignPadRatio());
		}
	}
}
