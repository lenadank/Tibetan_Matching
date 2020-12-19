package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import tau.cs.wolf.tibet.percentage_apbt.data.ArrInt;

public abstract class SlicableParserInt<SRC> implements SlicableParser<int[], SRC> {

	private final Pattern delimiter;
	
	public SlicableParserInt(Pattern delimiter) {
		this.delimiter = delimiter;
	}
	
	public SlicableParserInt() {
		this(Pattern.compile("\\s+"));
	}
	
	@Override
	public Slicable<int[]> parse(SRC src) {
		List<Integer> res = new ArrayList<Integer>();
		try (Scanner s = newScanner(src)) {
			s.useDelimiter(this.delimiter);
			while (s.hasNextInt()) {
				res.add(s.nextInt());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return new ArrInt(res);
	}

	protected abstract Scanner newScanner(SRC src) throws IOException;
}
