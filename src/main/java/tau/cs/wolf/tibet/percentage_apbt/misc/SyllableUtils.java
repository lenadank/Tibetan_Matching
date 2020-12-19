package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class SyllableUtils {
	public interface SyllableGrouper {
		public Integer getGroupIdx(int syllableIdx);
	}
	
	private abstract class FileStemMapper implements SyllableGrouper {
		protected FileStemMapper(File f) throws IOException {
			try (LineNumberReader reader = new LineNumberReader(new FileReader(f))) {
				String line;
				while((line = reader.readLine()) != null) {
					if (line.isEmpty()) {
						continue;
					}
					String[] split = line.split("\\s*,\\s*");
					if (split.length != 2) {
						throw new IllegalArgumentException("stem mapper file has more than 2 values in line: "+reader.getLineNumber());
					}
					add(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
				}
				postProcess();
			}
		}

		abstract protected void postProcess();

		protected abstract void add(int syllableIdx, int stemIdx);
	}
	
	public class StemMapper extends FileStemMapper {
		
		private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		public StemMapper(File f) throws IOException {
			super(f);
		}

		@Override
		public Integer getGroupIdx(int syllableIdx) {
			return this.map.get(syllableIdx);
		}

		@Override
		protected void add(int syllableIdx, int stemIdx) {
			map.put(syllableIdx, stemIdx);
		}

		protected void postProcess() {
			// do nothing
		}
	}
}
