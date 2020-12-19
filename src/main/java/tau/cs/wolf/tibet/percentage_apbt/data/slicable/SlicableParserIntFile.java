package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SlicableParserIntFile extends SlicableParserInt<File> {

	public SlicableParserIntFile(Pattern delimiter) {
		super(delimiter);
	}
	
	public SlicableParserIntFile() {
		super();
	}

	@Override
	protected Scanner newScanner(File f) throws FileNotFoundException {
		return new Scanner(f);
	}

}
