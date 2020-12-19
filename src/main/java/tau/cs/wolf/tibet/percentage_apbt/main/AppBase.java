package tau.cs.wolf.tibet.percentage_apbt.main;

import org.kohsuke.args4j.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsBase;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;
import tau.cs.wolf.tibet.percentage_apbt.misc.PropsBuilder;

public abstract class AppBase implements Runnable {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String CFG_PATH_VM_PROP_NAME = "percentage_apbt.cfgPath";
	
	protected final Props props;
	
	public AppBase(ArgsBase args, Props _props) {
		this.props = _props == null ? PropsBuilder.defaultProps() : _props;
		try {
			args.fillWithProps(this.props);
		} catch (CmdLineException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public AppBase(ArgsBase args) {
		this(args, null);
	}

}
