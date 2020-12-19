package tau.cs.wolf.tibet.percentage_apbt.preprocess;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.Slicable;
import tau.cs.wolf.tibet.percentage_apbt.main.args.ArgsPreprocess;
import tau.cs.wolf.tibet.percentage_apbt.misc.UnorderedIntPair;

import com.google.common.collect.BiMap;

public interface PreprocessDirStructureMarshaller {
	public void writeArgs(ArgsPreprocess args) throws IOException;
	public ArgsPreprocess readArgs() throws IOException;
	
	public void writeDocMap(BiMap<String, Integer> docs) throws IOException;
	public BiMap<Path, Integer> readDocsMap() throws IOException;
	
	public void writeDoc(int docId, Slicable<?> content) throws IOException;
	public Slicable<?> readDoc(int docId) throws IOException;
	
	public void writeGrp(int grpId, Set<UnorderedIntPair> grp) throws IOException;
	public Set<UnorderedIntPair> readGrp(int grpId) throws IOException;		
	
	public void writeGrpMap(Map<Integer, Set<UnorderedIntPair>> grpMap) throws IOException;
	public Map<Integer, Set<UnorderedIntPair>> readGrpMap() throws IOException;
	
	public void writeGrpIds(List<Integer> grpsIds) throws IOException;
	public List<Integer> readGrpIds() throws IOException;
	
}