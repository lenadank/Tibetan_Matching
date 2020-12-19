package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SlicableParserIntString extends SlicableParserInt<String> {

	public SlicableParserIntString(Pattern delimiter) {
		super(delimiter);
	}
	
	public SlicableParserIntString() {
		super();
	}
	
	@Override
	protected Scanner newScanner(String src) throws IOException {
		return new Scanner(src);
	}


}
