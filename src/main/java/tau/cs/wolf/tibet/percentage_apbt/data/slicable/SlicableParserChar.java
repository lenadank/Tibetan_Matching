package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;

import tau.cs.wolf.tibet.percentage_apbt.data.ArrChar;

public abstract class SlicableParserChar<SRC> implements SlicableParser<char[], SRC> {

	@Override
	public Slicable<char[]> parse(SRC src) {
		try {
			char[] charArr = getCharArr(src);
			return new ArrChar(charArr);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected abstract char[] getCharArr(SRC src) throws IOException;

}
