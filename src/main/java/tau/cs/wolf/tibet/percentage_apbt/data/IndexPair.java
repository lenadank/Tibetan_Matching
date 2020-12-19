package tau.cs.wolf.tibet.percentage_apbt.data;

import java.io.Serializable;

public class IndexPair implements Serializable, Comparable<IndexPair> {

	private static final long serialVersionUID = -3230705850675333275L;

	protected int idx1;
	protected int idx2;

	public IndexPair(int index1, int index2) {
		idx1 = index1;
		idx2 = index2;
	}
	
	public IndexPair(IndexPair o) {
		this.idx1 = o.idx1;
		this.idx2 = o.idx2;
	}

	public int getIndex1() {
		return idx1;
	}

	public int getIndex2() {
		return idx2;
	}

	public void shift(int val) {
		this.idx1 += val;
		this.idx2 += val;
	}
	
	public void incrementIndex1(int val) {
		this.idx1 += val;
	}
	
	public void incrementIndex2(int val) {
		this.idx2 += val;
	}

	public String toString() {
		return "(" + idx1 + "," + idx2 + ")";
	}

	@Override
	public int compareTo(IndexPair other) {
		int firstIndexCompare = Integer.compare(this.idx1, other.idx1);
		if (firstIndexCompare != 0) {
			return firstIndexCompare;
		}
		return Integer.compare(this.idx2, other.idx2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexPair other = (IndexPair) obj;
		if (idx1 != other.idx1)
			return false;
		if (idx2 != other.idx2)
			return false;
		return true;
	}
	
}
