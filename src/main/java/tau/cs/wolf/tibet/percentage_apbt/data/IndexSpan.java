package tau.cs.wolf.tibet.percentage_apbt.data;

public class IndexSpan extends IndexPair {

	private static final long serialVersionUID = -1;

	public IndexSpan(int spanStart, int spanEnd) {
		super(spanStart, spanEnd);
	}
	
	public IndexSpan(IndexSpan o) {
		super(o);
	}

	@Override
	public int getIndex1() {
		throw new UnsupportedOperationException("use span method instead");
	}

	@Override
	public int getIndex2() {
		throw new UnsupportedOperationException("use span method instead");
	}
	
	public int getStart() {
		return this.idx1;
	}
	
	public int getEnd() {
		return this.idx2;
	}

	public void shift(int val) {
		this.idx1 += val;
		this.idx2 += val;
	}
	
	public void incrementStart(int val) {
		this.idx1 += val;
	}
	
	public void incrementEnd(int val) {
		this.idx2 += val;
	}
	
	public boolean contains(IndexSpan other) {
		return this.idx1 <= other.idx1 && this.idx2 >= other.idx2;
	}
	
	public IndexSpan intersection(IndexSpan other) {
		int intersectionStart = Math.max(this.idx1, other.idx1);
		int intersectionEnd = Math.min(this.idx2, other.idx2);
		return intersectionEnd-intersectionStart<0 ? null : new IndexSpan(intersectionStart, intersectionEnd);
	}
	
	public int length() {
		return this.idx2 - this.idx1;
	}

}
