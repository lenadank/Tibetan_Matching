package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class HashMapHistogramTester {
	public static void main(String[] args){
		List<Integer> intLst = Arrays.asList(1, 2, 1, 2, 3, 4, 3, 1);
		IHistogram<Integer> intHist = new HashMapHistogram<>();
		for (int i : intLst){
			intHist.addItem(i);
		}
		if (intHist.getCountForItem(1) != 3){
			System.out.println("ERROR 1");
		}
		if (intHist.getCountForItem(5) != 0){
			System.out.println("ERROR 2");
		}
		Iterator<Integer> intHistIt = intHist.iterator();
		List<Integer> tmpList = new ArrayList<Integer>();
		while (intHistIt.hasNext()){
			tmpList.add(intHistIt.next());
		}
		if (tmpList.get(0) != 1){
			System.out.println("ERROR 3");
		}
		if (tmpList.size() != 4){
			System.out.println("ERROR 4");
		}
		
		
		IHistogram<String> stringHist = new HashMapHistogram<>();
		stringHist.addItem("abc");
		try{
			stringHist.addItemKTimes("de", 9);
			stringHist.addItemKTimes("abc", 3);
		}
		catch (IllegalKValue e){
			System.out.println("ERROR 5");
		}
		if (stringHist.getCountForItem("abc") != 4){
			System.out.println("ERROR 6");
		}
		System.out.println("done!");
	}
}
