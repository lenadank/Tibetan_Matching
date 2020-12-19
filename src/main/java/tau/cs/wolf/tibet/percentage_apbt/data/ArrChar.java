package tau.cs.wolf.tibet.percentage_apbt.data;

import java.io.Serializable;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;

public class ArrChar implements Slicable<char[]>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final char[] arr;
	private final int fromIdx;
	private final int length;
	
	
	public ArrChar(char[] arr) {
		this(arr, 0, arr.length);
	}
	
	public ArrChar(char[] arr, int fromIdx, int toIdx) {
		this.arr = arr;
		this.fromIdx = fromIdx;
		this.length = toIdx - fromIdx;
	}
	
	@Override
	public int length() {
		return this.length;
	}

	@Override
	public ArrChar slice(int start, int end) {
		return new ArrChar(this.arr, this.fromIdx+start, this.fromIdx+end);
	}

	@Override
	public boolean compare(int idx, Slicable<char[]> other, int otherIdx) {
		ArrChar o = (ArrChar) other;
		return this.arr[this.fromIdx+idx] == o.get()[o.fromIdx+otherIdx];
	}
	
	public char[] getArr() {
		return this.arr;
	}

	@Override
	public char[] get() {
		return this.arr;
	}
	
	@Override
	public String toString() {
		return new String(this.arr, this.fromIdx, this.length);
	}

	@Override
	public int getRealIndex(int i) {
		return i + this.fromIdx;
	}

}
