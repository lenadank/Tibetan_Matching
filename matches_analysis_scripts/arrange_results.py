import shared as shared
import sys
import subprocess
from subprocess import Popen

sub_folder = sys.argv[1]

print("!!!!!!!!!")
print("sub dir:", sub_folder)

string_to_remove_from_output = ['/a/home/cc/cs/turing/tibet/data/tokenize-flat/', \
                               ' (USE OF THIS SECTION REQUIRES COMPLETING AN AUTHORIZATION FORM From ACIP)'\
                                ]

def remove_strings_from_output(string):
    for s in string_to_remove_from_output:
        string = string.replace(s, "")
    return string

def sort_file_by_match_len(file_name, output_dir, output_file_name):
    f = open(file_name, "r")
    out_file_name = output_file_name.split("/")[-1].replace(".csv", "")
    lines = {}
    f.readline()
    for line in f:
        match = shared.create_match_from_merged_string(line)
        lines[match] = match.citation1.end - match.citation1.start
    sorted_matches = sorted(lines, key=lines.get, reverse=True)
    out_file_full_name = output_dir + "sorted_by_length/" + out_file_name + "3_.csv"
    out_file = open(out_file_full_name, "w")
    cnt = 0

    header = "other file, span-start,span-end,text\n"
    out_file.write(header)
    while(cnt < len(sorted_matches)):
        out_file.write(get_printable_line(sorted_matches[cnt], skip_first_column=True))
        out_file.write("\n\n")
        cnt += 1
    print("results are saved in: " + out_file_full_name)
    out_file.close()

def sort_file_by_second_file(file_name, output_dir, output_file_name):
    f = open(file_name, "r")
    out_file_name = output_file_name.split("/")[-1].replace(".csv", "")
    lines = {}
    f.readline()
    for line in f:
        match = shared.create_match_from_merged_string(line)
        lines[match] = match.citation1.full_source
    sorted_matches = sorted(lines, key=lines.get)
    out_file_full_name = output_dir + "sorted_by_file/" + out_file_name + "_1.csv"
    out_file = open(out_file_full_name, "w")

    header = "other file, span-start,span-end,text\n"
    out_file.write(header)
    cnt = 0
    while(cnt < len(sorted_matches)):
        out_file.write(get_printable_line(sorted_matches[cnt], skip_first_column=True))
        out_file.write("\n\n")
        cnt += 1
    print("results are saved in: " + out_file_full_name)
    out_file.close()


def sort_file_by_common_citation(file_name, output_dir, output_file_name):
    f = open(file_name, "r")
    out_file_name = output_file_name.split("/")[-1].replace(".csv", "")
    matches_with_overlaps = {}
    matches=[]
    f.readline()
    for line in f:
        matches.append(shared.create_match_from_merged_string(line))
    for i in range(len(matches)):
        matches_with_overlaps[matches[i]] = get_overlaps(i, matches)
    if len(matches_with_overlaps) == 0:
        return
    sorted_matches = sorted(matches_with_overlaps.items(), cmp=sort_by_overlap_comp, reverse=True)
    out_file_full_name = output_dir + "sorted_by_citations/" + out_file_name + "_2.csv"
    out_file = open(out_file_full_name, "w")
    cnt = 0
    header = "other file,first span - start,first span - end,first text,second span - start,second span - end,second text\n"
    out_file.write(header)
    prev_span = get_span(sorted_matches[0][0])
    while(cnt < len(sorted_matches)):
        curr_span = get_span(sorted_matches[cnt][0])
        # out_file.write("curr " + str(curr_span) + " prev " + str(prev_span) + "\n")
        if not spans_overlap(curr_span, prev_span):
            out_file.write("#"*10 + "," + "#"*10 + "," + "#"*10 + "\n")
        str_to_write = sorted_matches[cnt][0].get_in_csv_format(skip_first_3_columns=True)
        out_file.write(remove_strings_from_output(str_to_write))
        #out_file.write(str(lines_with_overlaps.get(sorted_lines[cnt][0])))
        prev_span = curr_span
        out_file.write("\n\n")
        cnt += 1
    print("results are saved in: " + out_file_full_name)
    out_file.close()

def get_span(match):
    return (int(match.citation1.start), int(match.citation1.end))

def sort_by_overlap_comp(item1, item2):
    cmp_res = cmp(item1[1], item2[1])
    if cmp_res == 0:
        span1 = get_span(item1[0])
        span2 = get_span(item2[0])
        cmp_res =  cmp(span2, span1) # since I'm using reverse in the sort function, the spans are switched to keep the earlier span first
        return cmp_res
    return cmp_res
    

def get_overlaps(match_index, matches):
    span1 = int(matches[match_index].citation1.start), int(matches[match_index].citation1.end)
    cnt = 0;
    for i in range(len(matches)):
        if i == match_index:
            continue
        span2 = int(matches[i].citation1.start), int(matches[i].citation1.end)
        if spans_overlap(span1, span2):
            cnt +=1
    return cnt
                                
def spans_overlap(span1, span2):
    start1, end1 = int(span1[0]),  int(span1[1])
    start2, end2 = int(span2[0]),  int(span2[1])
    return not ((start1 > end2) or (start2 > end1) or (end1 < start1) or (end2 < start1))
                               

def get_printable_line(match, skip_first_column=False):
     first_line = match.citation2.full_source + ","+ str(match.citation1.start) + "," + str(match.citation1.end) + "," + match.citation1.text
     if not skip_first_column:
         first_line = match.citation1.full_source + "," + first_line
     second_line = ",\n," + str(match.citation2.start) + "," + str(match.citation2.end) + "," + match.citation2.text
     result = first_line + "\n" + second_line +"\n"
     result = remove_strings_from_output(result)
     return result
    

def run_all():
    cnt = 1
    output_dir = "../../out/matches/sorted_results_" + sub_folder + "/"
    names_map = shared.build_file_names_map(shared.id_map_file)
    for i in shared.load_merged_file_names(shared.merged_results_folder):
        simple_file_name = str(int(i.split("/")[-1].replace("_Combined.csv", "")))
        output_file_name = remove_strings_from_output(names_map.get(simple_file_name)).replace(".txt", "")
        sort_file_by_second_file(i, output_dir,output_file_name)
        sort_file_by_common_citation(i, output_dir,output_file_name)
        sort_file_by_match_len(i, output_dir,output_file_name)
run_all()
