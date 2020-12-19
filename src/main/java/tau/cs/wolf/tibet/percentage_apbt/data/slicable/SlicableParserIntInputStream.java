package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SlicableParserIntInputStream extends SlicableParserInt<InputStream> {

	public SlicableParserIntInputStream(Pattern delimiter) {
		super(delimiter);
	}
	
	public SlicableParserIntInputStream() {
		super();
	}

	@Override
	protected Scanner newScanner(InputStream is) throws FileNotFoundException {
		return new Scanner(is);
	}

}
