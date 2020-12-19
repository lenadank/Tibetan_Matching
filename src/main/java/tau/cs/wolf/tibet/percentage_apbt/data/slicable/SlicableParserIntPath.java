package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SlicableParserIntPath extends SlicableParserInt<Path> {

	public SlicableParserIntPath(Pattern delimiter) {
		super(delimiter);
	}
	
	public SlicableParserIntPath() {
		super();
	}

	@Override
	protected Scanner newScanner(Path src) throws IOException {
		return new Scanner(src);
	}

}
