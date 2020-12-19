package tau.cs.wolf.tibet.percentage_apbt.main;

import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParser;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserCharFile;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserCharInputStream;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserCharPath;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserCharString;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserIntFile;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserIntInputStream;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserIntPath;
import tau.cs.wolf.tibet.percentage_apbt.data.slicable.SlicableParserIntString;

public class AppUtils {

	@SuppressWarnings("unchecked")
	public static <DATA, SRC> SlicableParser<? extends DATA, ? extends SRC> getParser(DataType dataType, SrcType srcType) {
		SlicableParser<? extends DATA, ? extends SRC> res = null;
		switch (dataType) {
			case INT:
				switch (srcType) {
					case INPUT_STREAM: 
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserIntInputStream();
						break;
					case FILE: 
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserIntFile();
						break;
					case STRING:
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserIntString();
						break;
					case PATH:
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserIntPath();
						break;
				}
				break;
			case CHAR:
				switch (srcType) {
					case INPUT_STREAM:
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserCharInputStream();
						break;
					case FILE:
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserCharFile();
						break;
					case PATH:  
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserCharPath();
						break;
					case STRING: 
						res = (SlicableParser<? extends DATA, ? extends SRC>) new SlicableParserCharString();
						break;
				}
				break;
		}
		if (res == null) {
			throw new IllegalArgumentException("Unknown dataType: "+dataType+", srcType: "+srcType+" combination.");
		}
		return res;
	}
	
	public static enum AppStage {
		// The order of the instances matter!!!
		APBT(1d), 
		UNION(2d), 
		ALIGNMENT(3d);
		
		private final double order;
		private AppStage(double order) {
			this.order = order;
		}
		public double order() {
			return this.order;
		}
	}

	public static enum SrcType {
		INPUT_STREAM,
		FILE,
		STRING,
		PATH
	}
	
	public static enum DataType {
		CHAR, 
		INT
	}

}
