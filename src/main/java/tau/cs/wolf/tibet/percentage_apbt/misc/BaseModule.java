package tau.cs.wolf.tibet.percentage_apbt.misc;

import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;

public class BaseModule {
	protected final Props props;
	protected final ArgsCommon args;
	
	protected BaseModule(Props props, ArgsCommon args) {
		this.props = props;
		this.args = args;
	}
}
