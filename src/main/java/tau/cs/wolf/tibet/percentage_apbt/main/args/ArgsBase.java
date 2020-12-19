package tau.cs.wolf.tibet.percentage_apbt.main.args;

import java.io.Serializable;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import tau.cs.wolf.tibet.percentage_apbt.misc.Props;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils.OutputStreamGobbler;

public class ArgsBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("deprecation")
	public ArgsBase(String[] args) throws CmdLineException {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			// parse the arguments.
			parser.parseArgument(args);
			if (this.help) {
				parser.printUsage(System.out);
				System.out.println();
			}
		} catch (CmdLineException e) {
			e.printStackTrace();
			OutputStreamGobbler osg = new OutputStreamGobbler();
			parser.printUsage(osg.get());
			throw new CmdLineException(e.getMessage()+"\n"+osg.toString());
		}
	}

	public ArgsBase() {
		// do nothing
	}

	@Option(name = "-h", aliases = { "-help", "--help" }, help = true, usage = "print this message")
	private boolean help = false;
	
	public void fillWithProps(Props props) throws CmdLineException {
		// do nothing
	}

}