package tau.cs.wolf.tibet.percentage_apbt.ranking.utils.stemming;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tau.cs.wolf.tibet.percentage_apbt.config.Directories;

public class StemUtils {
	//public static final String cleanTextDir = Directories.CLEAN_TEXT_DIR;
	public static final File stemsIndexDir = Directories.STEM_DIR;
	static
	{
		loadDada();
	}
	
	private static Map<Integer, String> intToSyllMap;
	private static Map<Integer, String> intToStemMap;
	private static Map<Integer, Integer> syllToStemIntMap;
	
	private static void loadDada(){
		try{
			intToSyllMap = new HashMap<>();
			intToStemMap = new HashMap<>();
			syllToStemIntMap = new HashMap<>();
			loadWordToIntMap("syllables.txt", intToSyllMap);
			loadWordToIntMap("stems.txt", intToStemMap);
			loadIntToIntMap("SylToStem.txt", syllToStemIntMap);
			//add ||
			intToSyllMap.put(-1, "||");
			intToStemMap.put(-1, "||");
			syllToStemIntMap.put(-1, -1);
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	private static void loadWordToIntMap(String fileName, Map<Integer, String> map) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(stemsIndexDir.toURI()).resolve(fileName), Charset.defaultCharset());
		for (String line: lines){
			String[] words = line.split(",");
			map.put(Integer.parseInt(words[1]), words[0]);
		}
	}
	private static void loadIntToIntMap(String fileName, Map<Integer, Integer> map) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(stemsIndexDir.toURI()).resolve(fileName), Charset.defaultCharset());
		for (String line: lines){
			String[] words = line.split(",");
			map.put(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
		}

	}
	
	public static String getStemForInt(int i){
		return intToStemMap.get(i);
	}
	
	public static String getStemSequenceForStr(String s, String delimiter){
		String[] integers = s.split(delimiter);
		String[] syllables = new String[integers.length];
		for (int i = 0; i < integers.length; i++){
			syllables[i] = getSyllForInt(Integer.parseInt(integers[i]));
		}
		return Arrays.toString(syllables).replaceAll(",", delimiter);
	}
	
	public static String getSyllForInt(int i){
		return intToSyllMap.get(i);
	}
	public static int getStemIntForSyllInt(int i){
		return syllToStemIntMap.get(i);
	}
	
}
