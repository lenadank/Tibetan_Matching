package tau.cs.wolf.tibet.percentage_apbt.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.data.AppResults;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsMain;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public abstract class AppCommon extends AppBase {

	private static final Logger logger = LoggerFactory.getLogger(AppCommon.class);
	
	private AppResults results;
	protected final ArgsMain args;
	
	AppCommon(ArgsMain args, Props _props) {
		super(args, _props);
		this.args = args;
		logger.info("setup with args: "+this.args);
	}
	
	public AppCommon(ArgsMain args) {
		this(args, null);
	}

	public AppResults getResults() {
		if (this.results == null) {
			throw new IllegalStateException("Cannot get results before run() was called");
		}
		return this.results;
	}
	
	public void writeResults() {
		if (this.results == null) {
			throw new IllegalStateException("Cannot write results before run() was called");
		}
		this._writeResults(this.results);
	}
	
	protected abstract void _writeResults(AppResults results);

	@Override
	public void run() {
		this.results = calcResults();
	}
	
	abstract protected AppResults calcResults();
}
