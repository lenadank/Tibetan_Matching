package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HashMapHistogram<T> implements IHistogram<T>{

	private Map<T, Integer> histMap;
	int cnt;
	
	public HashMapHistogram(){
		histMap = new HashMap<>();
	}
	
	@Override
	public void addItem(T item) {
		try{
			addItemKTimes(item, 1);
		}
		catch (IllegalKValue exp){
			//
		}
	}

	@Override
	public void addItemKTimes(T item, int k) throws IllegalKValue {
		Integer val = histMap.get(item);
		if (val == null){
			val = 0;
		}
		val += k;
		cnt +=k;
		histMap.put(item, val);	
	}

	@Override
	public Iterator<T> iterator() {
		return new HashMapHistogramIterator<>(histMap);
	}

	@Override
	public int getCountForItem(T item) {
		Integer val = histMap.get(item);
		return val != null? val : 0;
	}

	@Override
	public void addAll(Collection<T> items) {
		for (T item : items){
			addItem(item);
		}
		
	}

	@Override
	public void clear() {
		histMap.clear();	
	}

	@Override
	public Set<T> getItemsSet() {
		return histMap.keySet();
	}

	@Override
	public int getTotalCountNum() {
		return cnt;
	}
}
