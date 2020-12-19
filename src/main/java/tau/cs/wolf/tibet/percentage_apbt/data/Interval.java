package tau.cs.wolf.tibet.percentage_apbt.data;

public class Interval implements java.io.Serializable, Comparable<Interval> {

	private static final long serialVersionUID = -3230705851675333275L;

	public static Interval newIntervalBySpans(IndexSpan spanA, IndexSpan spanB) {
		IndexPair first = new IndexPair(spanA.getStart(), spanB.getStart());
		IndexPair last = new IndexPair(spanA.getEnd(), spanB.getEnd());
		return new Interval(first, last, spanA, spanB);
	}

	public static Interval newIntervalByStartEnd(IndexPair start, IndexPair end) {
		IndexSpan spanA = new IndexSpan(start.getIndex1(), end.getIndex1());
		IndexSpan spanB = new IndexSpan(start.getIndex2(), end.getIndex2());
		return new Interval(start, end, spanA, spanB);
	}

	private final IndexPair first;
	private final IndexPair last;
	private final IndexSpan span1;
	private final IndexSpan span2;

	private Interval(IndexPair start, IndexPair end, IndexSpan span1, IndexSpan span2) {
		this.span1 = span1;
		this.span2 = span2;
		this.first = start;
		this.last = end;
	}
	
	public Interval(Interval o) {
		this.span1 = new IndexSpan(o.span1);
		this.span2 = new IndexSpan(o.span2);
		this.first = new IndexPair(o.first);
		this.last = new IndexPair(o.last);
	}

	public IndexPair getStart() {
		return first;
	}

	public IndexPair getEnd() {
		return last;
	}

	public IndexSpan getSpan1() {
		return this.span1;
	}

	public IndexSpan getSpan2() {
		return this.span2;
	}
	
	
	public void shiftSpans(int span1Shift, int span2Shift) {
		this.shiftSpan1(span1Shift);
		this.shiftSpan2(span2Shift);
	}
	
	public void shiftSpan1(int shiftVal) {
		if (shiftVal != 0) {
			this.span1.shift(shiftVal);
			this.first.incrementIndex1(shiftVal);
			this.last.incrementIndex1(shiftVal);
		}
	}
	
	public void shiftSpan2(int shiftVal) {
		if (shiftVal != 0) {
			this.span2.shift(shiftVal);
			this.first.incrementIndex2(shiftVal);
			this.last.incrementIndex2(shiftVal);
		}
	}
	
	public int calcGapSpan1(Interval other) {
		return calcGapSpan(this.getSpan1(), other.getSpan1());
	}
	
	public int calcGapSpan2(Interval other) {
		return calcGapSpan(this.getSpan2(), other.getSpan2());
	}
	
	public boolean includes(Interval other) {
		return this.span1.contains(other.span1) && this.span2.contains(other.span2);
	}
	
	private int calcGapSpan(IndexSpan spanA, IndexSpan spanB) {
		IndexSpan intersection = spanA.intersection(spanB);
		if (intersection != null) {
			return 0;
		}
		return Math.max(spanA.getStart()-spanB.getEnd(), spanB.getStart()-spanA.getEnd());
		
		
	}

	@Override
	public String toString() {
		return first + " - " + last;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		return true;
	}

	@Override
	public int compareTo(Interval other) {
		int firstComparison = this.first.compareTo(other.first);
		if (firstComparison != 0) {
			return firstComparison;
		}
		return this.last.compareTo(other.last);
	}

}
