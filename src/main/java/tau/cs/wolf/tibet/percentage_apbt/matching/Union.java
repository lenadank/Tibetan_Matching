package tau.cs.wolf.tibet.percentage_apbt.matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tau.cs.wolf.tibet.percentage_apbt.data.IndexPair;
import tau.cs.wolf.tibet.percentage_apbt.data.IndexSpan;
import tau.cs.wolf.tibet.percentage_apbt.data.Interval;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchPair;
import tau.cs.wolf.tibet.percentage_apbt.data.MatchResult;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsCommon;
import tau.cs.wolf.tibet.percentage_apbt.misc.BaseModule;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;

public class Union extends BaseModule {

	public Union(Props props, ArgsCommon args) {
		super(props, args);
	}

	public List<MatchResult> uniteMatches(List<MatchResult> matches) {
		List<IndexPair> pairsToUnite = new ArrayList<IndexPair>();
		for (int i = 0; i < matches.size(); i++) {
			for (int j = i + 1; j < matches.size(); j++) {
				MatchPair pair = MatchPair.of(matches.get(i), matches.get(j));

				if (checkUnion(pair)) {
					pairsToUnite.add(new IndexPair(i, j));
				}

			}
		}

		return unitePairs(matches, pairsToUnite);
	}

	private boolean checkUnion(MatchPair pair) {
		if (!checkSegment(pair.left.getInterval().getSpan1(), pair.right.getInterval().getSpan1())) {
			return false;
		}
		if (!checkSegment(pair.left.getInterval().getSpan2(), pair.right.getInterval().getSpan2())) {
			return false;
		}
		return true;
	}

	private boolean checkSegment(IndexSpan spanA, IndexSpan spanB) {
		if (spanB.getStart() < spanA.getStart()) {
			return checkSegment(spanB, spanA);
		}

		if (spanA.getEnd() < spanB.getStart()) {
			return (spanB.getStart() - spanA.getEnd() < args.getMinDistanceUnion());
		}
		return true;
	}

	private List<MatchResult> unitePairs(List<MatchResult> matches, List<IndexPair> pairs) {

		Map<Integer, Integer> elementToGroup = new TreeMap<Integer, Integer>();
		Map<Integer, List<Integer>> groupToElements = new TreeMap<Integer, List<Integer>>();

		List<MatchResult> unitesMatches = new ArrayList<MatchResult>();

		for (int i = 0; i < matches.size(); i++) {
			elementToGroup.put(i, i);

			List<Integer> initElements = new ArrayList<>();
			initElements.add(i);
			groupToElements.put(i, initElements);
		}

		for (IndexPair pair : pairs) {
			int firstElement = pair.getIndex1();
			int secondElement = pair.getIndex2();

			int firstGroup = elementToGroup.get(firstElement);
			int secondGroup = elementToGroup.get(secondElement);

			if (firstGroup != secondGroup) {
				int newGroupName = Math.min(firstGroup, secondGroup);
				int deletedGroupName = Math.max(firstGroup, secondGroup);
				List<Integer> newGroupElements = groupToElements.get(newGroupName);
				newGroupElements.addAll(groupToElements.get(deletedGroupName));
				for (int element : newGroupElements) {
					elementToGroup.put(element, newGroupName);
				}
				groupToElements.remove(deletedGroupName);
			}
		}

		for (List<Integer> elements : groupToElements.values()) {
			MatchResult union = null;
			for (int element : elements) {
				if (union == null) {
					union = new MatchResult(matches.get(element));
					continue;
				}
				union = unitePair(union, matches.get(element));
			}
			unitesMatches.add(union);

		}
		return unitesMatches;
	}

	private MatchResult unitePair(MatchResult m1, MatchResult m2) {
		IndexSpan unitedSpan1 = closSpans(m1.getInterval().getSpan1(), m2.getInterval().getSpan1());
		IndexSpan unitedSpan2 = closSpans(m1.getInterval().getSpan2(), m2.getInterval().getSpan2());
		
		int span1Gap = m1.getInterval().calcGapSpan1(m2.getInterval());
		int span2Gap = m1.getInterval().calcGapSpan2(m2.getInterval());
		
		double distance = m1.getScore() + m2.getScore() + Math.max(span1Gap, span2Gap);
		
		double score = props.getComputeLevenshteinDistance() ? distance : -1;
		return new MatchResult(Interval.newIntervalBySpans(unitedSpan1, unitedSpan2), score);
	}

	private IndexSpan closSpans(IndexSpan spanA, IndexSpan spanB) {
		int start = Math.min(spanA.getStart(), spanB.getStart());
		int end = Math.max(spanA.getEnd(), spanB.getEnd());
		return new IndexSpan(start, end);
	}

}
