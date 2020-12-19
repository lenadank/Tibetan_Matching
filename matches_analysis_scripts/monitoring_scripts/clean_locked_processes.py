import sys

sub_dir = sys.argv[1]
print "!!!!!!!!!"
print "output dir:", sub_dir
out_dir_to_clean = "../../../out/processed_" + sub_dir + "/groups"
print "dir to clean", out_dir_to_clean
import os.path

sub_dirs = [x[0] for x in os.walk(out_dir_to_clean)]
cnt = 0
for sub_dir in sub_dirs:
    sub_folder_stripped = sub_dir.replace(out_dir_to_clean, "")
    if sub_folder_stripped == "":
        continue
    lock_file = sub_dir + "/" + "_lock"
    res_file = sub_dir + "/" + "result.txt"
    if os.path.isfile(lock_file):
        if not os.path.isfile(sub_dir + "/" + "_finish"):
            os.remove(lock_file)
            if os.path.isfile(res_file):
                os.remove(res_file)
            cnt+=1
print cnt
