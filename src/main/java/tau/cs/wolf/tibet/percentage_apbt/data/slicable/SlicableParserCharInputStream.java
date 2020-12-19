package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class SlicableParserCharInputStream extends SlicableParserChar<InputStream> {

	@Override
	protected char[] getCharArr(InputStream is) throws IOException {
		String text = IOUtils.toString(is);
		return text.toCharArray();
	}

}
