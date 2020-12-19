package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

public interface Slicable<R> {
	public int length();
	public Slicable<R> slice(int start, int end);
	public boolean compare(int idx, Slicable<R> other, int otherIdx);
	public R get();
	public int getRealIndex(int i);
}
