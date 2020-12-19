package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.util.AbstractMap;

public class SlicableEntry extends AbstractMap.SimpleEntry<String, Slicable<?>> {
	private static final long serialVersionUID = 1L;
	
	public SlicableEntry(String key, Slicable<?> value) {
		super(key, value);
	}
}