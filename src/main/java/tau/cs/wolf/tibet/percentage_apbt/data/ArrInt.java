package tau.cs.wolf.tibet.percentage_apbt.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;

public class ArrInt implements Slicable<int[]>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final int[] arr;
	private final int fromIdx;
	private final int length;
	
	
	public ArrInt(int[] arr) {
		this(arr, 0, arr.length);
	}
	
	
	public ArrInt(List<Integer> ints) {
		this(ArrInt.convertIntegers(ints));
	}
	
	public ArrInt(int[] arr, int fromIdx, int toIdx) {
		this.arr = arr;
		this.fromIdx = fromIdx;
		this.length = toIdx - fromIdx;
	}
	
	@Override
	public int length() {
		return this.length;
	}

	@Override
	public ArrInt slice(int start, int end) {
		return new ArrInt(this.arr, this.fromIdx+start, this.fromIdx+end);
	}

	@Override
	public boolean compare(int idx, Slicable<int[]> other, int otherIdx) {
		ArrInt o = (ArrInt) other;
		return this.arr[this.fromIdx+idx] == other.get()[o.fromIdx+otherIdx];
	}
	
	public int[] getArr() {
		return this.arr;
	}
	
	private static int[] convertIntegers(List<Integer> integers) {
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++) {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}


	@Override
	public int[] get() {
		return this.arr;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(this.arr, ' ', this.fromIdx, this.fromIdx+this.length);
	}

	@Override
	public int getRealIndex(int i) {
		return i + this.fromIdx;
	}

}
