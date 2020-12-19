package tau.cs.wolf.tibet.percentage_apbt.data.slicable;

public interface SlicableParser<DATA, SRC> {
	Slicable<DATA> parse(SRC src);

}