package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

import java.util.ArrayList;
import java.util.List;

import tau.cs.wolf.tibet.percentage_apbt.misc.Consumer;

public class ListConsumer implements Consumer<SlicableEntry> {
	private final List<SlicableEntry> list = new ArrayList<SlicableEntry>();
	
	@Override
	public void accept(SlicableEntry t) {
		this.list.add(t);
	}
	
	public List<SlicableEntry> get() {
		return this.list;
	}
}