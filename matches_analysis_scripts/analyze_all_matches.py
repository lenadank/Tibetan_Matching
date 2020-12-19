"""
This script uses the output of the merge_all_files script.
input: a folder with .csv files. Each csv file contains the matches for a single Tibetan file
"""

import shared as sh



def get_top_k_pairs(input_folder, k=10, min_match_len=10):
    matches_for_pair = dict()
    files = sh.load_merged_file_names(input_folder)
    for f in files:
        all_matches_for_file = sh.read_merged_matches_file(f)
        for match in all_matches_for_file:
            if len(match) < min_match_len:
                continue
            source1 = match.citation1.source
            source2 = match.citation2.source
            if source1 < source2:
                key = (source1, source2)
            else:
                key = (source2, source1)
            if key in matches_for_pair:
                matches_for_pair.get(key).append(match)
            else:
                matches_for_pair[key] = [match]
    sorted_items = sorted(matches_for_pair.items(), key=lambda x: len(x[1]), reverse=True)
    output_file_dir = input_folder.replace("merged", "analyzed")
    output_file = open(output_file_dir +  "top_" + str(k) + "_files.csv", "w")
    for i in xrange(min(len(sorted_items), k)):
        for match in sorted_items[i][1]:
            output_file.write(match.get_in_csv_format())
            output_file.write("\n\n")
        output_file.write("#######,#######,########,#######,######,########\n")
    output_file.close()


def get_top_k_matches(input_folder, sort_criteria, output_suffix, k=10, min_match_len=10):
    matches = list()
    files = sh.load_merged_file_names(input_folder)
    for f in files:
        all_matches_for_file = sh.read_merged_matches_file(f)
        for match in all_matches_for_file:
            if len(match) < min_match_len:
                continue
            else:
                matches.append(match)
    sorted_list = sorted(matches, key=sort_criteria, reverse=True)
    output_file_dir = input_folder.replace("merged", "analyzed")
    output_file = open(output_file_dir +  "top_" + str(k)+ "_by_" + output_suffix + "_matches.csv", "w")
    for i in xrange(min(len(sorted_list), k)):
        match = sorted_list[i]
        output_file.write(match.get_in_csv_format())
        output_file.write("\n\n")
    output_file.close()

def get_top_k_matches_by_score(input_folder, k=10, min_match_len=10):
    return get_top_k_matches(input_folder,  lambda x: x.score, "score", k, min_match_len)

def get_top_k_matches_by_length(input_folder, k=10, min_match_len=10):
    return get_top_k_matches(input_folder,  lambda x: len(x), "length", k, min_match_len)


get_top_k_pairs(sh.merged_results_folder, 10, 20)
get_top_k_matches_by_score(sh.merged_results_folder, 100, 20)
#get_top_k_matches_by_length(sh.merged_results_folder, 100, 20)