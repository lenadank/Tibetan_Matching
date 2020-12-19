import fnmatch
import os

input_dir = "../../out/matches/merged_Teng_only"


def get_merged_files_names(files_dir):
    matches = []
    for root, dirnames, filenames in os.walk(files_dir):
        for filename in fnmatch.filter(filenames, '*.csv'):
            matches.append(os.path.join(root, filename))
    return matches


def load_matches_for_a_single_file(curr_file):
    f = open(curr_file, "r")
    total_cnt = 0
    removals = {}
    inserts = {}
    replacements = {}
    total_match = 0
    res = {}
    f.readline() #skip the title
    for line in f:
        words = line.split(",")
        first_text = words[3]
        second_text = words[6]
        words_first_text = first_text.split(" ")
        words_second_text = second_text.split(" ")
        lev_res = levenshtein(words_first_text, words_second_text)
        total_cnt += lev_res[0]
        #inserts:
        for item in lev_res[1]:
            inserts[item] = inserts.get(item, 0) + 1
        #removes:
        for item in lev_res[2]:
            removals[item] = removals.get(item, 0) + 1
        #replacements:
        for item in lev_res[3]:
            replacements[item] = replacements.get(item, 0) + 1
        total_match += 1
    res["total_match"] = total_match
    res["total_cnt"] = total_cnt
    res["inserts"] = inserts
    res["removals"] = removals
    res["replacements"] = replacements
    return res


def levenshtein(s1, s2):
    distance = [[0 for x in range(len(s2) + 1)] for x in range(len(s1) + 1)]
    for i in range(len(s1) + 1):
        distance[i][0] = (i, s1[:i], [], [])
    for i in range(len(s2) + 1):
        distance[0][i] = (i, [], s2[:i], [])
    for i in range(1, len(s1) + 1):
        for j in range(1, len(s2) + 1):
            op1 = distance[i-1][j][0] + 1
            op2 = distance[i][j - 1][0] + 1
            op3 = distance[i - 1][j - 1][0] + (s1[i-1] != s2[j-1])
            best_option = min(op1, op2, op3)
            if op3 == best_option:
                rep_tmp = distance[i - 1][j - 1][3][:]
                if s1[i-1] != s2[j-1]:
                    rep_tmp.append((s1[i-1],s2[j-1]))
                distance[i][j] = (best_option, distance[i-1][j-1][1], distance[i-1][j-1][2], rep_tmp)
            elif op1 == best_option:
                distance[i][j] = (best_option, distance[i-1][j][1] + [s1[i-1]], distance[i-1][j][2], distance[i-1][j][3])
            else:
                distance[i][j] = (best_option, distance[i][j-1][1], distance[i][j-1][2] + [s2[j-1]], distance[i][j-1][3])
    #print_nice(distance, s1, s2)
    return distance[len(s1)][len(s2)];

def print_nice(distance, s1, s2):
    print "\t" + "_"+"\t",
    for s in s2:
        print s + "\t",
    print ""
    for i in range(len(distance)):
        word = "_"
        if i > 0:
            word = s1[i-1]
        print word + "\t",
        for item in distance[i]:
            print str(item[0]) + "\t",
        print ""
        #print word + "\t" + str(distance[i][0]).replace("[", "").replace("]", "").replace(" ", "\t")
    print "__________________________"


def analyse():
    merged_files = get_merged_files_names(input_dir)
    total_dif = 0
    total_metches = 0
    inserts = {}
    removals = {}
    replacements = {}
    for f in merged_files:
        res = load_matches_for_a_single_file(f)
        total_dif += res.get("total_cnt")
        total_metches += res.get("total_match")
        for item in res.get("inserts").items():
            inserts[item[0]] = inserts.get(item[0], 0) + item[1]
        for item in res.get("removals").items():
            removals[item[0]] = removals.get(item[0], 0) + item[1]
        for item in res.get("replacements").items():
            replacements[item[0]] = replacements.get(item[0], 0) + item[1]

    print "total_matches:", total_metches
    print "total_dif:", total_dif
    print "inserts:", len(inserts)
    print "removals:", len(removals)
    print "replacements:", len(replacements)

    print "\n#### first 10 inserts: ####"
    sorted_inserts = sorted(inserts.keys(), key=inserts.__getitem__, reverse=True)
    for i in xrange(10):
        print sorted_inserts[i], ":", removals[sorted_inserts[i]]

    print "#### first 10 removals: ####"
    sorted_removals = sorted(removals.keys(), key=removals.__getitem__, reverse=True)
    for i in xrange(10):
        print sorted_removals[i], ":", removals[sorted_removals[i]]

    print "#### first 10 replacements: ####"
    sorted_replacements = sorted(replacements.keys(), key=replacements.__getitem__, reverse=True)
    for i in xrange(10):
        print sorted_replacements[i], ":", replacements[sorted_replacements[i]]

analyse()
#print levenshtein(["a", "b", "c"], ["a", "c"])
#print levenshtein(["a", "b", "c", "d", "e", "g"], ["a", "c", "d", "f", "g"])