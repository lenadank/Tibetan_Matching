import glob
import re

input_dir = "../metadata/*.txt"
tenjur_dir = "../../../Tibet_data/Kangyur_Tenjur-CLEAN/tokenize-flat/"

metadata_files =  glob.glob(input_dir)

#devide into lines
for metadata_file in metadata_files:
    print metadata_file
    f = open(metadata_file, "r")
    f_out = open(metadata_file.replace(".txt", ".aligned"), "w")
    start = False
    for line in f:
        if not start and ("Bibliographical Title" in line or "Title Page Title" in line):
            start = True
        if start:
            line = line.replace("<start>", "\n<start>")
            f_out.write(line.strip())
    f_out.close()

def load_all_Tenj(tenj_dir):
    print input_dir +"/TEN*"
    files = glob.glob(tenj_dir +"/TEN*")
    res = {}
    for f in files:
        num = int(f.split("TD")[1].replace(".txt", "")[0:4])
        if num in res:
            res[num].append(f)
        else:
            res[num] = [f]
    print "keys: ", len(res)
    print "values: ", len(files)
    return res

def analyze_ten():
    all_tenj_files = load_all_Tenj(tenjur_dir)
    matched_tenj_files = set()
    input_dir = "../metadata/*Ten*.aligned"
    metadata_files =  glob.glob(input_dir)
    numbers_set = set()
    cnt = 0
    for metadata_file in metadata_files:
        print metadata_file
        f = open(metadata_file, "r")
        out_f = open(metadata_file.replace(".aligned", ".out"), "w")
        for line in f:
            start_indices = [m.start() for m in re.finditer("<start>", line)] + [len(line) + 1]
            if len(start_indices) == 1:
                continue
            list_for_line = []
            for i in xrange(len(start_indices)-1):
                curr_stack = []
                candidate = line[start_indices[i] : start_indices[i+1]+1]
                indices = [m.start() for m in re.finditer(">", candidate)] + [len(candidate)+1]
                if 0 not in indices:
                    indices = [0] + indices
                for j in xrange(len(indices)-1):
                    section = candidate[indices[j]:indices[j+1]+1]
                    if "<" in section and section.index("<") == 1:
                        continue
                    if section == "<start>":
                        continue
                    section = section.replace("</td>", "").replace(">", "")
                    if (len(section.strip()) == 0):
                        continue
                    curr_stack.append(section)
                    #print section
                #analyze stack
                if "Bibliographical Title" in curr_stack[0]:
                    for d in curr_stack:
                        if "</a" in d:
                            list_for_line.append("__________________")
                            break
                        if d.startswith("toh"):
                            try:
                                if "; " in d:
                                    d = d.split(";")[0]
                                number = int(d.split(":")[1].strip())
                                if number in all_tenj_files:
                                    matched_tenj_files.add(all_tenj_files.get(number))
                                    list_for_line.append("files: " + str(all_tenj_files.get(number)))
                                    numbers_set.add(number)
                            except:
                                print "failed parsing:", d
                        list_for_line.append(d)
                else:
                    list_for_line.append(">" + curr_stack[0])
            for l in list_for_line:
                out_f.write(l + "\n")
        print "matched", len(numbers_set), "numbers out of", len(all_tenj_files)
        print "unmatches"
        for i in all_tenj_files:
            if all_tenj_files.get(i) not in matched_tenj_files:
                print i, all_tenj_files.get(i)
        out_f.close()

def analyze_kan():
    input_dir = "../metadata/*Kan*.aligned"
    metadata_files =  glob.glob(input_dir)
    cnt = 0
    for metadata_file in metadata_files:
        print metadata_file
        f = open(metadata_file, "r")
        out_f = open(metadata_file.replace(".aligned", ".out"), "w")
        for line in f:
            start_indices = [m.start() for m in re.finditer("<start>", line)] + [len(line) + 1]
            if len(start_indices) == 1:
                continue
            list_for_line = []
            for i in xrange(len(start_indices)-1):
                curr_stack = []
                candidate = line[start_indices[i] : start_indices[i+1]+1]
                indices = [m.start() for m in re.finditer(">", candidate)] + [len(candidate)+1]
                if 0 not in indices:
                    indices = [0] + indices
                for j in xrange(len(indices)-1):
                    section = candidate[indices[j]:indices[j+1]+1]
                    if "<" in section and section.index("<") == 1:
                        continue
                    if section == "<start>":
                        continue
                    section = section.replace("</td>", "").replace("</div", "").replace(">", "")
                    if (len(section.strip()) == 0):
                        continue
                    curr_stack.append(section)
                    #print section
                #analyze stack
                if ":" not in str(curr_stack):
                    list_for_line.append(">" + curr_stack[0])
                else:
                    for d in curr_stack:
                        if "Order Pecha Hardcopy" in d:
                            break
                        list_for_line.append(d)
                    list_for_line.append("__________________")
            for l in list_for_line:
                out_f.write(l + "\n")
        out_f.close()

analyze_ten()
#load_all_Tenj(tenjur_dir)