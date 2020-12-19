export _JAVA_OPTIONS="-Xmx4g"
export TIBET_LIB=../lib
export TIBET_OUT=../out
export TIBET_DATA=/Users/lena/Documents/Tibet_Orna/Tibet_data/Kangyur_Tenjur-CLEAN/
export TIBET_CONF=../src/site

java  -cp "../target/classes:$TIBET_LIB/*"  \
	-Dpercentage_apbt.cfgPath=$TIBET_CONF/cfg.txt \
	-Dlog4j.configuration=$TIBET_CONF/log4j.xml \
	-DtfDir=$ENV(TIBET_HOME)/data/tf_idf_data/ -Xmx4G\
	tau.cs.wolf.tibet.percentage_apbt.main.AppPreprocess  --dataType INT  \
	-inDir $TIBET_OUT/preprocessed_out \
	-o $TIBET_OUT/processed_out \
	--appStage ALIGNMENT \

	
	
	
