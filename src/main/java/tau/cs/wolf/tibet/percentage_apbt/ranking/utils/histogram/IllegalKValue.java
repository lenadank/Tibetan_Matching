package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram;

public class IllegalKValue extends Exception {
	private static final long serialVersionUID = 1L;

	public IllegalKValue(int k){
		super("Illegal k value: " + k);
	}
}
