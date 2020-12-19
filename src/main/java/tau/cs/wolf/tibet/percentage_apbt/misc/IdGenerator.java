package tau.cs.wolf.tibet.percentage_apbt.misc;

public interface IdGenerator<R> {
	public int nextId(R obj);
	
	
	public static class SequentialIdGenerator<R> implements IdGenerator<R> {
	
		private final int step;
		private int current;
		
		public SequentialIdGenerator(int start, int step) {
			this.step = step;
			this.current = start - step;
		}
		
		@Override
		public int nextId(R obj) {
			this.current += this.step;
			return this.current;
		}
		
	}

}