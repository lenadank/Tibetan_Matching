
package tau.cs.wolf.tibet.percentage_apbt.matching;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tau.cs.wolf.tibet.percentage_apbt.data.IndexPair;
import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsBase;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

/**
 * This class implements the APBT algorithm for finding in two strings, all the
 * pairs of common approximate patterns, which satisfy the following criteria:
 * the length of both patterns in a pair is at least minLength, and the edit
 * distance between patterns (in a pair) is at most maxDiff. Max length of
 * patterns is set to 300 characters. If patterns of length greater than 300 are
 * expected, then they can be obtained either by appropriate post-processing or
 * by re-runing this program with a bigger value for MAX_LENGTH.
 *
 * @author marina
 */

@SuppressWarnings("all")
public class Apbt<R extends java.lang.reflect.Array> implements Runnable {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * This constant defines the size of a chunk of the matching matrix to be
	 * processed simultaneously. This speeds up the calculation by decreasing
	 * the memory used. The simultaneously processed part of a matrix is of
	 * size: CHUNK_SIZE*_seq1.length, where _seq1.length is the length of the
	 * first string.
	 */
	private ProcessType processType;
	private int chunkSize;
	private int maxLength;
	private int maxMatrixCols;
	private SortedSet<Interval> _solutions = new TreeSet<Interval>();
	private int maxDiff;
	private int maxDiffPlus1;
	private int minLength;

	private int curMatrixCols = -1;
	private boolean[][] matrix;
	private Slicable<R> seq1;
	private Slicable<R> seq2;
	private int[][][] state;
	private boolean[] emptyRow;

	@SuppressWarnings("unchecked")
	public void setup(Slicable<? extends R> seq1, Slicable<? extends R> seq2, ProcessType processBy, ArgsCommon args, Props props) {
		if (args.getMinLength() > props.getMaxMatchLength()) {
			throw new IllegalStateException("minimum match legth exceeds maximum alowed match length property");
		}
		
		switch (processBy) {
			case ROW:
				this.seq1 = (Slicable<R>) seq1;
				this.seq2 = (Slicable<R>) seq2;
				break;
			case COL:
				this.seq1 = (Slicable<R>) seq2;
				this.seq2 = (Slicable<R>) seq1;
				break;
			default: throw new IllegalArgumentException("Unknown "+ProcessType.class.getSimpleName()+": "+this.processType);
		}
		this.processType = processBy;

		this.minLength = args.getMinLength();
		this.maxDiff = args.getMaxError();
		this.maxDiffPlus1 = args.getMaxError()+1;

		this.maxLength = props.getMaxMatchLength();
		this.chunkSize = props.getChunkSize();
		
		this.maxMatrixCols = this.maxLength + this.chunkSize;
		
		this.emptyRow = new boolean[Math.min(this.maxMatrixCols, this.seq2.length())];
	}

	
	/********************************************************************************************
	 * PUBLIC
	 ********************************************************************************************/

	@Override
	public void run() {

		this.matrix = new boolean[this.seq1.length()][];
		
		int numChunks = (int) Math.ceil(((double) this.seq2.length()) / this.chunkSize);
		
		for (int chunkIdx = 0; chunkIdx < numChunks; chunkIdx++) {
			int startJ = chunkIdx * this.chunkSize;
			logger.trace(String.format("processing chunk %d/%d",  chunkIdx+1, numChunks));
			process(startJ);
		}
	}

	/**
	 * 
	 * @return sorted List of intervals in a two-dimensional space. The Interval
	 *         contains the coordinates of the start (start position in string1,
	 *         start position in string 2) and of the end (end position in
	 *         string1, end position in string 2).
	 */
	public List<Interval> getSolutions() {
		return new ArrayList<Interval>(_solutions);
	}

	/********************************************************************************************
	 * PRIVATE
	 ********************************************************************************************/

	/**
	 * This function processes the parts of the matrix starting from particular
	 * position in the second string.
	 * 
	 * @param startJ
	 *            - start position in a second string
	 */

	private void process(int startJ) {

		if (startJ > this.seq2.length() - this.minLength) {
			return;
		}
		
		this.curMatrixCols = Math.min(this.maxMatrixCols, this.seq2.length()-startJ);
				
		initializeMatrix(startJ, startJ + this.curMatrixCols);

		int rowIdxLimit = this.seq1.length() - this.minLength;
		int colIdxLimit = this.curMatrixCols - this.minLength;
		
		for (int i = 0; i <= rowIdxLimit; i++) {
			for (int j = 0; j <= colIdxLimit; j++) {
				if (this.matrix[i][j]) {
					createPaths(i, j, startJ);
				}
			}
			// reset the row of state array to use with the next added (i+this.maxLength)-th row:
			this.state[(i) % this.maxLength] = new int[this.curMatrixCols][];

		}
	}

	private int startI;
	private int startJ;
	private int shiftJ;
	
	/**
	 * Initializes a new path of ML=1, EN=0, starting from current true cell of
	 * the matrix
	 */
	private void createPaths(int _startI, int _startJ, int _shiftJ) {
		this.startI = _startI;
		this.startJ = _startJ;
		this.shiftJ = _shiftJ;
		continuePath(_startI, _startJ, 1, 0, true);
	}

	/**
	 * The main recursion - expanding of the path. Stop conditions: the path
	 * cannot be expanded without an additional error, OR a better path has been
	 * collected ending at point (currI, currJ)
	 */
	
	private void continuePath(int currI, int currJ, int currlen, int currdiff, boolean continueFurther) {
		if (currlen >= this.maxLength) {
			return;
		}
	
		if (continueFurther) {
			if (currlen > 1) {
				int[] instate = this.state[currI % this.maxLength][currJ];
	
				if (instate == null) {
					instate = new int[2];
					if (currI - startI <= currJ - startJ) {
						instate[0] = currdiff;
						instate[1] = this.maxDiffPlus1;
					} else {
						instate[1] = currdiff;
						instate[0] = this.maxDiffPlus1;
					}
					this.state[currI % this.maxLength][currJ] = instate;
				} else { // check what is the best (minimum diff) checked for this point
					if (currI - startI <= currJ - startJ) {
						if (instate[0] <= currdiff) {
							return;
						} else {
							instate[0] = currdiff;
						}
	
					} else {
						if (instate[1] <= currdiff) {
							return;
						} else {
							instate[1] = currdiff;
						}
					}
	
					this.state[currI % this.maxLength][currJ] = instate;
	
				}
	
			}
	
			// define bounds of the target square
			int LT_I = Math.min(currI + 1, this.seq1.length() - 1);
			int LT_J = Math.min(currJ + 1, this.curMatrixCols - 1);
	
			int RT_J = Math.min(LT_J + this.maxDiff + 1 - currdiff, this.curMatrixCols);
			int LB_I = Math.min(LT_I + this.maxDiff + 1 - currdiff, this.seq1.length());
	
			int RB_J = RT_J;
	
			int currJBound = RB_J;
			int currIBound = LB_I;
	
			// explore the main diagonal
			boolean stop = false;
	
			// eladsh
//			for (int i = currI + 1, j = currJ + 1; i < Math.min(currI + this.maxDiff + 2 - currdiff, this.seq1.length())
//					&& j < Math.min(currJ + this.maxDiff + 2 - currdiff, this.seq2.length()) && !stop; i++, j++) {
			for (int i = currI + 1, j = currJ + 1; i < Math.min(currI + this.maxDiff + 2 - currdiff, this.seq1.length())
					&& j < Math.min(currJ + this.maxDiff + 2 - currdiff, this.matrix[i].length) && !stop; i++, j++) {
				if (this.matrix[i][j]) {
	
					continuePath(i, j, Math.min(i - startI, j - startJ) + 1,
							Math.max(i - currI, j - currJ) - 1 + currdiff, true);
					stop = true;
					currJBound = j;
					currIBound = i;
				}
			}
	
			for (int i = currIBound + 1; i < LB_I; i++) {
				if (this.matrix[i][currJBound]) {
					continuePath(i, currJBound, Math.min(i - startI, currJBound - startJ) + 1,
							Math.max(i - currI, currJBound - currJ) - 1 + currdiff, false);
				}
			}
	
			for (int j = currJBound + 1; j < RT_J; j++) {
				if (this.matrix[currIBound][j]) {
					continuePath(currIBound, j, Math.min(currIBound - startI, j - startJ) + 1,
							Math.max(currIBound - currI, j - currJ) - 1 + currdiff, false);
				}
			}
	
			// add an edge from next k upper and lower diagonals
			for (int k = 1; k < this.maxDiff + 1 - currdiff; k++) {
				// lower diagonals
				stop = false;
				for (int i = LT_I + k, j = LT_J; i < LB_I && j < currJBound && !stop; i++, j++) {
					if (this.matrix[i][j]) {
	
						continuePath(i, j, Math.min(i - startI, j - startJ) + 1,
								Math.max(i - currI, j - currJ) - 1 + currdiff, true);
						if (j < currJBound) {
							currJBound = j;
							for (int m = i + 1; m < LB_I; m++) {
								if (this.matrix[m][currJBound])
									continuePath(m, currJBound,
											Math.min(m - startI, currJBound - startJ) + 1,
											Math.max(m - currI, currJBound - currJ) - 1 + currdiff, false);
							}
	
						}
						stop = true;
					}
				}
	
				// upper diagonals
				stop = false;
				for (int i = LT_I, j = LT_J + k; i < currIBound && j < RT_J && !stop; i++, j++) {
					if (this.matrix[i][j]) {
	
						continuePath(i, j, Math.min(i - startI, j - startJ) + 1,
								Math.max(i - currI, j - currJ) - 1 + currdiff, true);
						if (i < currIBound) {
							currIBound = i;
							for (int m = j + 1; m < RT_J; m++) {
	
								if (this.matrix[currIBound][m]) {
									continuePath(currIBound, m,
											Math.min(currIBound - startI, m - startJ) + 1,
											Math.max(currIBound - currI, m - currJ) - 1 + currdiff, false);
								}
							}
	
						}
	
						stop = true;
					}
				}
			}
		}
	
		// the maximal path can not continue. Check if this is a solution
		if (currlen >= this.minLength) {
			addToSolutions(startI, startJ + shiftJ, currI, currJ + shiftJ);
		}
	
	}

	private void initializeMatrix(int from, int to) {
		Map<Object, boolean[]> symbolPositions = new HashMap<Object, boolean[]>(100);
		for (int j = from; j < to; j++) {
			
			Object curr = Array.get(this.seq2.get(), j);
			boolean[] row = (boolean[]) symbolPositions.get(curr);
			if (row == null) {
				row = new boolean[this.curMatrixCols];
				symbolPositions.put(curr, row);
			}
			row[j - from] = true;
		}
		// remove -1 key since it indicates an unknown
		symbolPositions.remove(-1);

		for (int i=0; i<this.seq1.length(); i++) {
			boolean[] row = symbolPositions.get(Array.get(this.seq1.get(), i));
			if (row != null) {
				this.matrix[i] = row;
			} else {
				this.matrix[i] = this.emptyRow;
			}
		}

		this.state = new int[this.maxLength][Math.min(this.chunkSize + this.maxLength, this.seq2.length())][];

	}
	
	private void addToSolutions(int startI, int startJ, int currI, int currJ) {

		IndexPair start = null;
		IndexPair end = null;
		switch (this.processType) {
			case ROW:
				start = new IndexPair(startI, startJ);
				end = new IndexPair(currI, currJ);
				break;
			case COL :
				start = new IndexPair(startJ, startI);
				end = new IndexPair(currJ, currI);
				break;
			default: throw new IllegalArgumentException("Unknown "+ProcessType.class.getSimpleName()+": "+this.processType);
		}

		Interval interval = Interval.newIntervalByStartEnd(start, end);
		
		Set<Interval> solutionsToRemove = new TreeSet<Interval>(); 
		
		for (Interval solution: this._solutions) {
			if (solution.includes(interval)) {
				return;
			}
			if (interval.includes(solution)) {
				solutionsToRemove.add(solution);
			}
		}
		
		this._solutions.removeAll(solutionsToRemove);
		this._solutions.add(interval);
	}

}
