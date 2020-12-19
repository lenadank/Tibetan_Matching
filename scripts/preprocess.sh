export _JAVA_OPTIONS="-Xmx4g"
export TIBET_LIB=../lib
export TIBET_OUT=../out
export TIBET_DATA=/Users/lena/Documents/Tibet_Orna/Tibet_data/Kangyur_Tenjur-CLEAN/
export TIBET_CONF=../src/site

java  -cp "../target/classes:$TIBET_LIB/*"  \
	-Dpercentage_apbt.cfgPath="$TIBET_CONF/cfg.txt"  \
	-Dlog4j.configuration=$TIBET_CONF/log4j.xml  \
	tau.cs.wolf.tibet.percentage_apbt.main.AppPreprocess  --dataType INT  \
	-groupSize 1000  \
	-inRootDir $TIBET_DATA/enum_stem-flat  \
	-inPath1 '*D3940*,*D3934*,*D4021*,*D4027*,*D4032*,*D4049*,*D4053*,*D4421*,*D4092*,*D3860*,*D2901*'  \
	-inPath2 '*.txt'  \
	-uniqueGroups 'true'  \
	-outDir $TIBET_OUT/preprocessed_out \

