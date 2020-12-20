# Find parallel passages in Tibetan corpus


# text preprocessing

Clone https://github.com/danielhers/tibetan-cleaning and execute ```./preprocess.sh raw``` (```raw``` is the folder with all the unprocessed texts.
This will generate the enum-stem folder (all the text is stemmed and each step is represented by an integer).


# running the apbt algorithm to find all the parallel passages

This code is adapted from:
https://github.com/shakedel/Tibet

The algorithm is executed in two stages:
1. Run the following script for preprocessing:
```apbt_scripts/preprocess.sh```
in this script, you should configure your directories and runtime parameters.

2. Run the following script for the processing:
```apbt_scripts/process.sh```
in this script, you should configure your directories and runtime parameters.


# analyzing matches
Follow the instructions in matches_analysis_scripts/Readme.txt to generate the output.