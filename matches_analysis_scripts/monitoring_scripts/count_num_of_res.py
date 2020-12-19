import fnmatch
import os
import subprocess
from subprocess import Popen
import sys
import glob


matches_dir = "../../../out/matches/matched_" + sys.argv[1]

def load_files(files_dir):
    files_cnt = 0;
    total_cnt = 0;
    above_2000 = 0;
    max_cnt = 0;
    sub_dirs = [x[0] for x in os.walk(files_dir)]
    for sub_dir in sub_dirs:
        sub_folder_stripped = sub_dir.replace(files_dir, "").replace("/", "")
        if sub_folder_stripped == "":
            continue
        files_cnt += 1
        files_in_sub_dir = glob.glob(sub_dir + "/*.*")
        num_of_files = len(files_in_sub_dir)
        total_cnt += num_of_files
        max_cnt = max(max_cnt, num_of_files)
        if num_of_files > 2000:
            above_2000 += 1
    print "total:", files_cnt, "files"
    print "average:", total_cnt/files_cnt
    print "above 2000:", above_2000
    print "max:", max_cnt
        
             


load_files(matches_dir)
