1. run matches_to_txt_csv.py subfolder_preprocess subfolder_process 
(for example: matches_to_txt_csv.py Keng_only Keng_only 

this creates the textual matches files out of the spans files. the results are stored in out/matches/matched_final
for each file we create a folder that will contain the relevant matches (since each pair appears only once, a folder for the n'th file will not contain all matches for n)

2. run merge_all_files.py subfolder [group1] [group2]
this merged all matches for the nth file into a single results file. each raw, besides the span and the text also holds the information about the file the match was found in.
the results are stored in merged_final

it is possible to set two groups to merge only matches from different groups.
use the split_ids script to create the ids list, and send them as arguments to the merge script

3. run arrange_results split
this sorts the results according to the popularity of each match