package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HashMapHistogramIterator<T> implements Iterator<T>{
	Iterator<T> it;

	public HashMapHistogramIterator(Map<T, Integer> map) {
		
		final Map<T, Integer> histMap = map;
		Comparator<T> comparatorC = new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int keysComparison = histMap.get(o1).compareTo(histMap.get(o2));
				return -keysComparison;
			}
		};
		List<T> tmpList = new ArrayList<>();
		tmpList.addAll(map.keySet());
		Collections.sort(tmpList, comparatorC);
		it = tmpList.iterator();
	}
	
	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public T next() {
		return it.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
		
	}
}
