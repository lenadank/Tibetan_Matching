import sys
import os
from glob import glob
import shared as shared




def slice_from_artice(article, start, end):
    res = ""
    for i in range(start, end):
        res += article[i] + " "
    return res

def matches_to_text_for_dir(files_map, input_dir, output_dir):
    print("writing results to " + output_dir)
    results = [y for x in os.walk(input_dir) for y in glob(os.path.join(x[0], '*.txt'))]
    print len(results), "files to process"
    i = 0
    for file_name in results:
        i+= 1
        if i % 200 == 0:
            """sys.stdout.write("finished: %d%% files  \r" % (i))
            sys.stdout.flush()"""
            print "finished", i
        f = open(file_name, "r")
        for line in f:
            analyze_single_match(line,  files_map, output_dir)
                
def analyze_single_match(line, files_map, output_dir):
    words = line.split("\t")
    output_file_name = words[0] + "_" + words[1] +".match"
    out_sub_dir_name = output_dir + "" + words[0]
    if not os.path.exists(out_sub_dir_name) :
        os.makedirs(out_sub_dir_name)
    output_file_name_with_dir = out_sub_dir_name + "/" + output_file_name
    if os.path.isfile(output_file_name_with_dir):
        #this files was already analysed
        return
    first_file = files_map.get(words[0])
    second_file = files_map.get(words[1])
    if first_file == None or second_file == None:
        print "fail getting article files for", words[0], words[1]
        print "files map len = ", len(files_map)
    article1 = open(first_file, 'r') # change this
    article2 = open(second_file, 'r') # change this
    str_article1 = article1.read().split()
    str_article2 = article2.read().split()
    output_csv = open(output_file_name_with_dir, 'w')
    #create a directory for the output
    output_csv.write('article1txt,article2txt,score,id\n')
    start = False
    count = 0
    for word in words[2:]:
        if not start and word == "ALIGNED:":
            start = True
            continue
        if start:
            word = word.strip()
            if len(word) == 0:
                continue
            arr = word.split(',')
            output_csv.write(arr[0] + "," + arr[2] + ",")
            output_csv.write(slice_from_artice(str_article1, int(arr[0]), int(arr[2])))
            output_csv.write(',')
            output_csv.write(arr[1] + "," + arr[3] + ",")
            output_csv.write(slice_from_artice(str_article2, int(arr[1]), int(arr[3])))
            output_csv.write(',')
            output_csv.write(arr[4].split()[0])
            output_csv.write(','+ str(count) + '\n')
            count += 1
    output_csv.close()


        
files_map = shared.build_file_names_map(shared.id_map_file)
matches_to_text_for_dir(files_map, shared.processed_results_dir, shared.matches_results_dir)

"""
if __name__ == '__main__':
    article1_filename = sys.argv[1]
    article2_filename = sys.argv[2]
    matches_filename = sys.argv[3]
    output_filename = sys.argv[4]
    convert_matches_to_txt(article1_filename,article2_filename,matches_filename,output_filename)
"""
