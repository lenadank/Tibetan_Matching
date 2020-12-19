from __future__ import print_function
import shared as shared
import os
import sys
import glob

sub_folder = sys.argv[1]

print("!!!!!!!!!")
print("sub dir:", sub_folder)
    

row_headers_for_merged_file = "firstFile,secondFile,firstFileFullName,SecondFileFullName,firstSpan start, firstSpan end, firstText, secondSpan start, secondSpan end, secondText,score\n"

def merge_for_single_file(first_file_id, matches_dir, output_dir, min_match_length, files_map):
    relevant_results = glob.glob(matches_dir + "*/" + first_file_id + "_*/*.match")
    relevant_results.extend(glob.glob(matches_dir + "*/*_" + first_file_id +".match"))
    assert len(relevant_results) != 0 , "there are no results for file: " + first_file_id
    res = {}
    reverse = False
    current_index = 0
    for matches_file in relevant_results:
        # from the results file we want to extract the file names of the two compared files
        if matches_file.startswith(str(first_file_id) + "_" ):
            reverse = False
            other_file_id = matches_file.split("_")[1].replace(".match", "")
        else:
            reverse = True
            other_file_id = matches_file.split("/")[-1].split("_")[0]
        res_file = open(matches_file, "r")
        res_file.readline() #skip title line
        for line in res_file:
            parts = line.split(",")
            if int(parts[1]) - int(parts[0]) < min_match_length:
                continue #remove too short matches
            file1 = parts[0] + "," + parts[1] + "," + parts[2]
            file2 = parts[3] + "," + parts[4] + "," + parts[5]
            if (reverse):
                line = file2 + "," + file1 + "," + parts[6]
            else:
                line = file1 + "," + file2 + "," + parts[6]
            line = first_file_id + "," + other_file_id + "," + files_map.get(first_file_id) +", " + files_map.get(other_file_id)+"," + line
            score = float(line.split(",")[-1])
            res[line]=score
    sorted_lines = sorted(res, key=res.get, reverse=True)
    out_file = open(output_dir + "/"+ first_file_id + "_Combined.csv", "w")
    out_file.write(row_headers_for_merged_file)
    cnt = 0
    while(cnt < 1000 and cnt < len(sorted_lines)):
        out_file.write(sorted_lines[cnt])
        out_file.write("\n")
        cnt += 1
    out_file.close()


def load_files(files_dir, first_group, min_match_lenth, files_map):
    sub_dirs = [x[0] for x in os.walk(files_dir)]
    assert len(sub_dirs) != 0, "the directory " + files_dir + " is empty!"
    point_to_print = 20
    step = len(first_group)/point_to_print
    i = 0
    for id in first_group:
        i += 1
        id = str(int(id))
        merge_for_single_file(id, files_dir, shared.merged_results_folder,  min_match_lenth, files_map)
        if i %  step == 0:
            per = int(i/(float(len(first_group)))*100)
            merged_str = "merged %" + str(per)
            back = "\b"*len(merged_str)
            #print(merged_str+back)
            #print("step")
            #print("merged " + str(per)+ "%" , end='\r')
            print (merged_str)
    print('done!')

def load_group(file_name):
    res = []
    f = open(file_name, "r")
    for line in f:
        res.append(line.split(",")[0])
    return res

def run_all(min_match_length, files_map):
    if len(sys.argv) > 2:
        first_group = load_group(sys.argv[2])
        second_group = load_group(sys.argv[3])
        print("loaded groups", len(first_group), len(second_group))
    else:
        first_group = None
        second_group = None
    load_files(shared.matches_results_dir, first_group, min_match_length, files_map)

def create_one_merge_file(min_matches):
    all_merged_files = shared.load_merged_file_names(shared.merged_results_folder)
    out_file = open(shared.merged_results_folder+"../"+shared.sub_folder+"_all_merged.csv", "w")
    out_file.write(row_headers_for_merged_file.rstrip() + "," + "numOfMatches\n")
    for f in all_merged_files:
        res = shared.read_merged_matches_file(f)
        if len(res) < min_matches:
            continue
        d = {}
        for r in res:
            d[r.citation2.source] = d.get(r.citation2.source, 0) + 1
        for r in res:
            out_file.write(r.get_in_csv_format() + "," + str(d[r.citation2.source]))
            out_file.write("\n")
    out_file.close()


files_map = shared.build_file_names_map(shared.id_map_file)
run_all(min_match_length = 10, files_map=files_map)
#create_one_merge_file(min_matches = 20)