package tau.cs.wolf.tibet.percentage_apbt.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.misc.Utils;

public class MatchesContainer {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ConcurrentLinkedQueue<MatchResult> matches;
	private boolean shutdown = false;
	
	public MatchesContainer(Utils.Formatter<MatchResult> formatter, double maxIntersection) {
		matches = new ConcurrentLinkedQueue<MatchResult>();
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
	
	private void assertNotShutdown() {
		if (this.shutdown) {
			throw new IllegalStateException("cannot accept results after shutdown!");
		}
	}
	
	private void assertShutdown() {
		if (!this.shutdown) {
			throw new IllegalStateException("must be shutdown after all results has been passed!");
		}
	}
	
	public List<MatchResult> getResults() {
		assertShutdown();
		return new ArrayList<MatchResult>(this.matches);
	}
	
	public synchronized void addResult(MatchResult newMatch) {
		this.assertNotShutdown();
		matches.add(newMatch);
		

	}
}
