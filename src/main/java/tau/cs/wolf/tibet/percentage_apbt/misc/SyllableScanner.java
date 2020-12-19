package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;



public class SyllableScanner {
	public static void main(String[] args) throws IOException {
		File f = new File("src/test/resources/char/in1.txt");
		Map<String, Integer> syllables = new SyllableScanner().scan(f);
		System.out.println(syllables.keySet().size()+" distinct syllables");
	}
	
	Map<String, Integer> sylableCount = new TreeMap<String, Integer>();
	
	public Map<String, Integer> scan(File f) throws IOException {
		try (Reader reader = new FileReader(f)) {
			return this.scan(reader);
		}
	}
	
	public Map<String, Integer> scan(InputStream is) throws IOException {
		try (Reader reader = new InputStreamReader(is)) {
			return this.scan(reader);
		}
	}
	
	public Map<String, Integer> scan(String str) throws FileNotFoundException {
		return this.scan(new StringReader(str));
		
	}
	
	public Map<String, Integer> scan(Readable r) throws FileNotFoundException {
		try (Scanner s = new Scanner(r)) {
			s.useDelimiter(Pattern.compile("([^a-zA-Z'])+"));
			while (s.hasNext()) {
				String sylable = s.next();
				Integer curCount = this.sylableCount.get(sylable);
				if (curCount == null) {
					this.sylableCount.put(sylable, 1);
				} else {
					this.sylableCount.put(sylable, ++curCount);
				}
			}
		}
		return this.sylableCount;
	}
}
