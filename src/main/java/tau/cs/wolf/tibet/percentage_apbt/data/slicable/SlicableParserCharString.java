package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;

public class SlicableParserCharString extends SlicableParserChar<String> {

	@Override
	protected char[] getCharArr(String str) throws IOException {
		return str.toCharArray();
	}

}
