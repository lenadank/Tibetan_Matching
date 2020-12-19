package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;


public class PropsBuilder {
	
	public static final String CFG_PATH_VM_PROP_NAME = "percentage_apbt.cfgPath";
	
	private final Props props;
	
	private PropsBuilder(Properties props) {
		this.props = new PropsImpl(props);
	}
	
	
	public static Props newProps(Properties props) {
		return new PropsBuilder(props).props;
	}

	public static Props defaultProps() {
		Properties props = Utils.propsFromVmArg(CFG_PATH_VM_PROP_NAME, true);
		return PropsBuilder.newProps(props);
	}
	
	public static Props newProps(InputStream is) throws IOException {
		Properties props = new Properties();
		props.load(is);
		return newProps(props);
	}
	
	public static Props newProps(File f) throws IOException {
		try (InputStream is = new FileInputStream(f)) {
			Properties props = new Properties();
			props.load(is);
			return newProps(props);
		}
	}
	
	public static Props newProps(String vmArgName) throws FileNotFoundException, IOException {
		String pathToProps = System.getProperty(vmArgName);
		if (pathToProps == null) {
			LoggerFactory.getLogger(Props.class).warn("Missing properties file, using default settings");
			return defaultProps();
		}
		return newProps(new File(pathToProps));
	}
	
}