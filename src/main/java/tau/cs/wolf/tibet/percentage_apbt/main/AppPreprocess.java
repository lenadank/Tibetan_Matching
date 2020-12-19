package tau.cs.wolf.tibet.percentage_apbt.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kohsuke.args4j.CmdLineException;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParser;
import tau.cs.wolf.tibet.percentage_apbt.main.AppUtils.SrcType;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsPreprocess;
import tau.cs.wolf.tibet.percentage_apbt.misc.IdGenerator;
import tau.cs.wolf.tibet.percentage_apbt.misc.Props;
import tau.cs.wolf.tibet.percentage_apbt.misc.UnorderedIntPair;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshaller;
import tau.cs.wolf.tibet.percentage_apbt.preprocess.PreprocessDirStructureMarshallerImpl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;



/*
 * Todo lena:
 * 1. rewrite the group generation mechanism. Should support: two exclusive groups, On group (all against all). 
 * 
 */
public class AppPreprocess extends AppBase {

	protected final ArgsPreprocess args;
	private final SortedSet<Integer> inDocIds1 = new TreeSet<Integer>();
	private final SortedSet<Integer> inDocIds2 = new TreeSet<Integer>();
	
	private final IdGenerator<Set<UnorderedIntPair>> grpIdGenerator = new IdGenerator.SequentialIdGenerator<>(0, 1);
	
	private final PreprocessDirStructureMarshaller marshaller;

	public AppPreprocess(ArgsPreprocess args, Props _props) {
		super(args, _props);
		this.args = args;
		this.marshaller = new PreprocessDirStructureMarshallerImpl(args.getOutDir());
	}

	@Override
	public void run() {
		try {
			runWithIOException();
		} catch(IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void runWithIOException() throws IOException {
		
		marshaller.writeArgs(this.args);
		
		// create docs dir
		Path docsDir = this.args.getOutDir().resolve("docs");
		try {
			Files.createDirectories(docsDir);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		
		// create docs map
		{
			IdGenerator<Path> docIdGenerator = new IdGenerator.SequentialIdGenerator<>(0, 1);
			BiMap<String, Integer> docs = HashBiMap.create();
			addDocs(docs, this.args.calcInPaths1(), this.inDocIds1, docIdGenerator);
			addDocs(docs, this.args.calcInPaths2(), this.inDocIds2, docIdGenerator);
			marshaller.writeDocMap(docs);
		}
		
		// create grps map
		{
			Map<Integer, Set<UnorderedIntPair>> grps = addGrps();
			List<Integer> grpsList = new ArrayList<>(grps.keySet());
			this.marshaller.writeGrpMap(grps);
			this.marshaller.writeGrpIds(grpsList);
		}

	}

	private Map<Integer, Set<UnorderedIntPair>> addGrps() throws IOException {
		Map<Integer, Set<UnorderedIntPair>> grps = new TreeMap<>();
		
		int maxGrpSize = this.args.getMaxGrpSize();
		int halfGrpSize = maxGrpSize/2;
		int len = (int) Math.floor(Math.sqrt(maxGrpSize));
		
//		Set<UnorderedIntPair> allPairs = new UnorderedIntPair.UnorderedIntPairSet();
		Set<UnorderedIntPair> partialGrp = null;
		
		boolean[] hasDocIdsRows;
		boolean[] hasDocIdsCols;
		
//		{
//			//in
//			if (this.inDocIds1.last() > this.inDocIds2.last()) {
//				hasDocIdsRows = generateHasDocId(this.inDocIds2);
//				hasDocIdsCols = generateHasDocId(this.inDocIds1);
//			} else {
//				hasDocIdsRows = generateHasDocId(this.inDocIds1);
//				hasDocIdsCols = generateHasDocId(this.inDocIds2);
//			}
//		}
		
		int pairsCnt = 0;
		Set<UnorderedIntPair> grp = new UnorderedIntPair.UnorderedIntPairSet();
		UnorderedIntPair pair = new UnorderedIntPair(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Set<Integer> intersection = new HashSet<>();
		intersection.addAll(Sets.intersection(this.inDocIds1, this.inDocIds2));
		logger.info("group sizes: " + this.inDocIds1.size() + " ; " + this.inDocIds2.size());
		//d1 is always smaller then d2
		for (int d1 : this.inDocIds1){   
			for (int d2: this.inDocIds2){ 
				if (this.args.getUniqueGroups()){
					if (intersection.contains(d2)){
						continue;
					}
				}
				else if (intersection.contains(d1) && intersection.contains(d2)){
					if (d1 >= d2){
						System.out.println("skipping pair (" + d1 + "," + d2 + ")");
						continue;
					}
				}
				pairsCnt++;
				pair.set(d1, d2);
//				boolean isNewPair = allPairs.add(pair);
//				if (isNewPair) {
				
				if (grp.size()  > maxGrpSize){
					int grpId = writeGrp(grp);
					grps.put(grpId, grp);
					grp = new UnorderedIntPair.UnorderedIntPairSet(); 
				}
				grp.add(pair);
			}
		}
		if (grp.size() > 0){
			int grpId = writeGrp(grp);
			grps.put(grpId, grp);
			System.out.println("generating group id =" + grpId);
		}
		
/*		
		for (int startRow=0; startRow<hasDocIdsRows.length; startRow+=len) {
			for (int startCol=startRow+1; startCol<hasDocIdsCols.length; startCol+=len) {
				
				Set<UnorderedIntPair> grp = new UnorderedIntPair.UnorderedIntPairSet();
				UnorderedIntPair pair = new UnorderedIntPair(Integer.MAX_VALUE, Integer.MAX_VALUE);
								
				for (int row=startRow; row<startRow+len && row<hasDocIdsRows.length; row++) {
					for (int col=Math.max(startCol, row+1); col<startCol+len && col<hasDocIdsCols.length; col++) {
						
						boolean hasDocId1 = hasDocIdsRows[row];
						boolean hasDocId2 = hasDocIdsCols[col];
						if ((!hasDocId1) || (!hasDocId2) || (col<=row)) {
							continue;
						}
						pair.set(row, col);
//						boolean isNewPair = allPairs.add(pair);
//						if (isNewPair) {
							grp.add(pair);
//						}
					}
				}
				
				if (grp.size() < halfGrpSize) {
					if (partialGrp == null) {
						partialGrp = grp;
						continue;
					} else {
						grp.addAll(partialGrp);
						partialGrp = null;
					}
				}
				int grpId = writeGrp(grp);
				grps.put(grpId, grp);
			}
			
		}
		*/
		System.out.println("Generated " + grps.size() + " groups ; " + pairsCnt + " pairs ");
//		if (partialGrp != null) {
//			int grpId = writeGrp(partialGrp);
//			grps.put(grpId, partialGrp);
//		}
		return grps;
	}

	private boolean[] generateHasDocId(SortedSet<Integer> inDocIds) {
		boolean[] res = new boolean[inDocIds.last()+1];
		{
			for (Integer docId: this.inDocIds1) {
				res[docId] = true;
			}
		}
		return res;
	}

	private int writeGrp(Set<UnorderedIntPair> grp) throws IOException {
		int grpId = this.grpIdGenerator.nextId(grp);
		this.marshaller.writeGrp(grpId, grp);
		return grpId;
	}

	private void addDocs(BiMap<String,Integer> docs, Set<Path> inPaths,
			SortedSet<Integer> inDocIds, IdGenerator<Path> idGenerator) throws IOException {
		@SuppressWarnings("unchecked")
		SlicableParser<?, Path> parser = (SlicableParser<?, Path>) AppUtils.getParser(args.getDataType(), SrcType.PATH);
		for (Path path: inPaths) {
			Integer docId = docs.get(path.toAbsolutePath().toString());
			if (docId == null) {
				docId = idGenerator.nextId(path);
				logger.info("assigning ID: "+docId+" to doc: "+path);
				docs.put(path.toAbsolutePath().toString(), docId);
				Slicable<?> content = parser.parse(path);
				this.marshaller.writeDoc(docId, content);
			}
			inDocIds.add(docId);
		}
	}

	public static void main(String[] args) {
		try {
			AppPreprocess app = new AppPreprocess(new ArgsPreprocess(args), null);
			app.run();
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
}
