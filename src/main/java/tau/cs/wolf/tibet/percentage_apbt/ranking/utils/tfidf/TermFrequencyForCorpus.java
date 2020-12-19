package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.tfidf;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tau.cs.wolf.tibet.percentage_apbt.config.Directories;
import tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram.HashMapHistogram;
import tau.cs.wolf.tibet.percentage_apbt.ranking.utils.histogram.IHistogram;
import tau.cs.wolf.tibet.percentage_apbt.ranking.utils.stemming.StemUtils;

public class TermFrequencyForCorpus {
	private static final String mainDir = "C:\\data\\Kangyur_Tenjur-CLEAN-2016-05-14\\enumerate\\enumerate"; //this is for learning tf-idf
	private static final String outputDir = "";
	private static final String tfDFFileName = Directories.TF_DIR + "/tf.txt";
	public static final int DOC_NUM_IN_CORPUS = 4282;
	public static final String ngramDelimiter = ":";

	private static Map<String, Integer> tfMap;
	private static Map<String, Integer> dfMap;

	static{
		tfMap = new HashMap<>();
		dfMap = new HashMap<>();

		try {
			loadTFDFCounts(1);
			loadTFDFCounts(2);
			loadTFDFCounts(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private static void loadTFDFCounts(int n) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(getTfFileName(n)), Charset.defaultCharset());
		for (int i = 1; i < lines.size(); i++){
			String[] words = lines.get(i).split(",");
			tfMap.put(words[0], Integer.parseInt(words[2]));
			dfMap.put(words[0], Integer.parseInt(words[3]));
		}
	}
	
	private static String getTfFileName(int n){
		return tfDFFileName + n;
	}
	public static int getTFInCorpusForStem(int... stemAsInt){
		return tfMap.get(stemAsInt);
	}
	
	public static int getDFForStem(int... stemAsInt){
		Integer val = dfMap.get(getKeyForNgram(stemAsInt));
		if (val == null){
			return 1;
		}
		return val;
	}
	
	
	private static String getKeyForNgram(int... stemAsInt){
		return Arrays.toString(stemAsInt).replaceAll(", ", ngramDelimiter).replace("[", "").replace("]", "");
	}

	private static String getKeyForNgram(String... stemAsInt){
		return Arrays.toString(stemAsInt).replaceAll(", ", ngramDelimiter).replace("[", "").replace("]", "");
	}

	
	public static void main(String[] args) throws IOException{
		countTFDF(1);
		countTFDF(2);
		countTFDF(3);
	}
	
	
	
	

	
	private static void countTFDF(int n) throws IOException{
		System.out.println("creating tf/ds file for n=" + n);
		List<String> files = getAllTextFilesDirs();
		IHistogram<String> stemHist = new HashMapHistogram<>();
		IHistogram<String> dfHist = new HashMapHistogram<>();

		int numOfDocuments = 0;
		String last = "";
		for (String filePath : files){
			
			List<String> ngram = new ArrayList<String>();
			numOfDocuments++;
			Set<String> stemsForFile = new HashSet<>();
			List<String> fileContent = Files.readAllLines(Paths.get(filePath), Charset.defaultCharset());
			for (String line : fileContent){
				String[] stemsInLine = line.split(" ");
				for (String stemInLine: stemsInLine){
					if (ngram.size() == n){
						ngram.remove(0);
						ngram.add(stemInLine);
						String ngramStr = getKeyForNgram(ngram.toArray(new String[0]));
						last = ngramStr;
						stemHist.addItem(ngramStr);
						stemsForFile.add(ngramStr);
					}
					else{
						ngram.add(stemInLine);
					}
				}
			}
			//System.out.println(last);
			//update the df histogram
			for(String stemInt : stemsForFile){
				dfHist.addItem(stemInt);
			}
		}
		List<String> stemsOut = new ArrayList<>();
		stemsOut.add("stemId,stem,count,df");
		for(String stem: stemHist){
			stemsOut.add(stem + "," + StemUtils.getStemSequenceForStr(stem, ngramDelimiter) + ","+ stemHist.getCountForItem(stem) + "," + dfHist.getCountForItem(stem));
		}
		Files.write(Paths.get(outputDir, getTfFileName(n)), stemsOut, Charset.defaultCharset());
		System.out.println("# of documents in corpus: " + numOfDocuments);
	}
	
	
	
	private static List<String> getAllTextFilesDirs(){
		List<String> filePaths = getAllTextFiles(new File(mainDir));
		return filePaths;
		
	}

	private static List<String> getAllTextFiles(File dir) {
		List<String> paths = new ArrayList<String>();
		if (dir.isFile()){
			if (dir.getAbsolutePath().endsWith(".txt")){
				paths.add(dir.getAbsolutePath());
			}
		}
		else{
			for (File f : dir.listFiles()){
				paths.addAll(getAllTextFiles(f));
			}
		}
		return paths;
		
	}
}
