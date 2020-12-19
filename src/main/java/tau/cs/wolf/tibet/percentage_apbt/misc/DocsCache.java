package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshaller;

public class DocsCache {

	private final PreprocessDirStructureMarshaller marshaller;
	private final DocsLruMap map;
	
	public DocsCache(PreprocessDirStructureMarshaller marshaller, long maxTotalValuesLength) {
		this.marshaller = marshaller;
		this.map = new DocsLruMap(maxTotalValuesLength);
	}

	public Slicable<?> get(int docId) throws IOException {
		Slicable<?> res = this.map.get(docId);
		if (res == null) {
			res = this.marshaller.readDoc(docId);
			this.map.put(docId, res);
		}
		return res;
	}

	private static class DocsLruMap extends LinkedHashMap<Integer, Slicable<?>> {

		private static final long serialVersionUID = 1L;
		private final long maxTotalValuesLength;

		
		public DocsLruMap(long maxTotalValuesLength) {
			super(16, 0.75f, true);
			this.maxTotalValuesLength = maxTotalValuesLength;
		}

		protected boolean removeEldestEntry(Map.Entry<Integer, Slicable<?>> eldest) {
			long curTotalLength = 0;
			for (Slicable<?> value: this.values()) {
				curTotalLength += value.length();
			}
			return curTotalLength >= this.maxTotalValuesLength;
		}
		
		

	}
	
}
