package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class UnorderedIntPair implements Comparable<UnorderedIntPair>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public int lower;
	public int higher;
	
	public UnorderedIntPair(int num1, int num2) {
		if (num1 >= num2) {
			this.higher = num1;
			this.lower = num2;
		} else {
			this.higher = num2;
			this.lower = num1;
		}
	}
	
	@Override
	public String toString() {
		return "["+lower +","+higher+"]";
	}

	public void set(int num1, int num2) {
		if (num1 >= num2) {
			this.higher = num1;
			this.lower = num2;
		} else {
			this.higher = num2;
			this.lower = num1;
		}
	}

	@Override
	public int compareTo(UnorderedIntPair o) {
		int comp = Integer.compare(this.higher, o.higher);
		if (comp != 0) {
			return comp;
		}
		return Integer.compare(this.lower, o.lower);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + higher;
		result = prime * result + lower;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnorderedIntPair other = (UnorderedIntPair) obj;
		if (higher != other.higher)
			return false;
		if (lower != other.lower)
			return false;
		return true;
	}

	public static class UnorderedIntPairSet implements Set<UnorderedIntPair>, Serializable {
	
		private static final long serialVersionUID = 1L;
		
		private final Map<Integer,Set<Integer>> map = new TreeMap<>();
		private int size = 0;
		
		@Override
		public int size() {
			return this.size;
		}
	
		@Override
		public boolean isEmpty() {
			return this.size()==0;
		}
	
		@Override
		public boolean contains(Object o) {
			UnorderedIntPair pair = (UnorderedIntPair) o;
			Set<Integer> set = this.map.get(pair.higher);
			return set==null ? false : set.contains(pair.lower);
		}
	
		@Override
		public void clear() {
			this.map.clear();
		}
	
	
		@Override
		public boolean add(UnorderedIntPair pair) {
			Set<Integer> set = this.map.get(pair.higher);
			if (set == null) {
				set = new TreeSet<>();
				this.map.put(pair.higher, set);
			}
			boolean added = set.add(pair.lower);
			if (added) {
				this.size++;
			}
			return added;
		}
	
		@Override public boolean addAll(Collection<? extends UnorderedIntPair> pairs) { 
			boolean res = false;
			for (UnorderedIntPair pair: pairs) {
				res |= this.add(pair);
			}
			return res;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Entry<Integer, Set<Integer>> entry: this.map.entrySet()) {
				int firstDocId = entry.getKey();
				for (Integer secondDocId: entry.getValue()) {
					sb.append(String.format("(%d,%d), ", firstDocId, secondDocId));
				}
			}
			return sb.toString();
		}
		
		@Override public Iterator<UnorderedIntPair> iterator() { 
			return new UnorderedIntPairSetIterator(this.map);
		}
		@Override public Object[] toArray() { throw new UnsupportedOperationException(); } 
		@Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
		@Override public boolean remove(Object o) { throw new UnsupportedOperationException(); } 
		@Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); } 
		@Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		@Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		
	}

	private static final class UnorderedIntPairSetIterator implements
			Iterator<UnorderedIntPair> {
		
		private final Iterator<Entry<Integer, Set<Integer>>> entryIterator;
		
		private Integer curFirst = null;
		private Iterator<Integer> curPairIterator = null;
		
		public UnorderedIntPairSetIterator(Map<Integer, Set<Integer>> map) {
			this.entryIterator = map.entrySet().iterator();
			if (entryIterator.hasNext()) {
				Entry<Integer, Set<Integer>> entry = entryIterator.next();
				this.curFirst = entry.getKey();
				this.curPairIterator = entry.getValue().iterator();
			}
		}
	
		@Override
		public boolean hasNext() {
			return this.curFirst!=null && this.curPairIterator.hasNext();
		}
	
		@Override
		public UnorderedIntPair next() {
			UnorderedIntPair res = new UnorderedIntPair(this.curFirst, this.curPairIterator.next());
			if (!this.curPairIterator.hasNext()) {
				if (!this.entryIterator.hasNext()) {
					this.curFirst = null;
					this.curPairIterator = null;
				} else {
					Entry<Integer, Set<Integer>> entry = this.entryIterator.next();
					this.curFirst = entry.getKey();
					this.curPairIterator = entry.getValue().iterator();
				}
			}
			
			return res;
		}
	}
	
}