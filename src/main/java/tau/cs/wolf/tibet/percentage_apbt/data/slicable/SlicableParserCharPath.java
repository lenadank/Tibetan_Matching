package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

public class SlicableParserCharPath extends SlicableParserChar<Path> {

	@Override
	protected char[] getCharArr(Path path) throws IOException {
		String text = IOUtils.toString(path.toUri().toURL());
		return text.toCharArray();
	}

}
