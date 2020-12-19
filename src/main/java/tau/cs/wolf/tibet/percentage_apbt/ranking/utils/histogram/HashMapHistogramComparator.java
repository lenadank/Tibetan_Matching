package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram;

import java.util.Comparator;
import java.util.Map;

public class HashMapHistogramComparator<T> implements Comparator<T>{
	Map<T, Integer> histMap;
	
	public HashMapHistogramComparator(Map<T, Integer> map) {
		histMap = map;
	}
	@Override
	public int compare(T o1, T o2) {
		int keysComparison = histMap.get(o1).compareTo(histMap.get(o2));
		return -keysComparison;
	}
	
	
}
