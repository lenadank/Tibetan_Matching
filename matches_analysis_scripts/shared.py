import fnmatch
import os
import sys


sub_folder = sys.argv[1]
print("!!!!!!!!!!")
print("sub_folder = ", sub_folder)


###################
### directories ###

id_map_file = "../../out/preprocessed_" + sub_folder + "/docs/map/id_map.txt"
merged_results_folder = "../../out/matches/merged_" + sub_folder + "/"
matches_results_dir = "../../out/matches/matched_" + sub_folder + "/"
processed_results_dir = "../../out/processed_" + sub_folder + "/"

######################
class Citation:
    def __init__(self, start, end, text, source, full_source=""):
        if not isinstance(start, int):
            raise ValueError("wrond start value in Cictation __init__" + start)
        if not isinstance(end, int):
            raise ValueError("wrond end value in Cictation __init__" + end)
        self.start = start
        self.end = end
        self.text = text
        self.source = source
        self.full_source = full_source

    def __repr__(self):
        res = "source: " + (self.source if self.full_source == "" else self.full_source) + " ; span: (" + str(self.start) + "," + str(self.end) + ")"
        res += "\n"
        res += "text: " + self.text
        return res

    def __len__(self):
        return self.end - self.start + 1


class Match:
    def __init__(self, citation1, citation2, score):
        if not isinstance(score, float):
            raise ValueError("wrond score value in Match __init__" + score)
        self.citation1 = citation1
        self.citation2 = citation2
        self.score = score

    def __repr__(self):
        res = repr(self.citation1) + "\n" + repr(self.citation2) + "\nscore: " + str(self.score) + "\n-----------"
        return res

    def reverse(self):
        self.citation1, self.citation2 = self.citation2, self.citation1

    def __len__(self):
        return min(len(self.citation1), len(self.citation2))

    def get_in_csv_format(self, skip_first_3_columns=False):
        res = ""
        if not skip_first_3_columns:
            res += self.citation1.source + "," + self.citation2.source + ","
            res += self.citation1.full_source + ","
        res += self.citation2.full_source + ","
        res += str(self.citation1.start) + "," + str(self.citation1.end) +',' + self.citation1.text + ","
        res += str(self.citation2.start) + "," + str(self.citation2.end) + ',' + self.citation2.text + ","
        res += str(self.score)
        return res


def create_match_from_merged_string(input_string):
    words = input_string.split(",")
    if (len(words) != 11):
        raise ValueError("error parsing merges result\n" + input_string)
    c1 = Citation(int(words[4]), int(words[5]), words[6], words[0], words[2])
    c2 = Citation(int(words[7]), int(words[8]), words[9], words[1], words[3])
    m = Match(c1, c2, float(words[10]))
    return m


def read_merged_matches_file(file_name):
    f = open(file_name, "r")
    f.readline()  # skip title
    res = []
    for line in f:
        res.append(create_match_from_merged_string(line))
    return res


def load_merged_file_names(files_dir):
    matches = []
    for root, dirnames, filenames in os.walk(files_dir):
        for filename in fnmatch.filter(filenames, '*.csv'):
            matches.append(os.path.join(root, filename))
    return matches




"""
this method loads the mapping file generated in the preprocessing stage
it returns a map: file number (str) -> real file name
"""
def build_file_names_map(map_file):
    f = open(map_file, "r")
    d = {}
    orig_str = "enum_stem"
    new_str = "tokenize"
    for line in f:
        for i in range(len(line)):
            if line[i].isdigit():
                break;
        line = line[i:]
        index_to_split = line.index(":")
        key = line[:index_to_split].strip()
        key = str(int(key))
        value = line[index_to_split+1:].replace(orig_str, new_str).strip()
        d[key]=value
    return d




