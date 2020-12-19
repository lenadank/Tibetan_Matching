package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class SlicableParserCharFile extends SlicableParserChar<File> {

	@Override
	protected char[] getCharArr(File src) throws IOException {
		try (InputStream is = new FileInputStream(src)) {
			String text = IOUtils.toString(is);
			return text.toCharArray();
		}
	}

}
