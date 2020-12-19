import shared as shared
from pylab import plot,show
from numpy import vstack,array
from numpy.random import rand
from scipy.cluster.vq import kmeans2,vq
from scipy.sparse import csr_matrix
import scipy
from sklearn.metrics.pairwise import cosine_similarity
from scipy import sparse

import numpy as np


vector_size = 0
f = open("../../out/Tang_Kenj_Split/Kang_ids.txt", "r")
for line in f:
    vector_size += 1

backward_tejn_alignment = {}
f = open("../../out/Tang_Kenj_Split/Tenj_ids.txt", "r")
for line in f:
    words = line.strip().split(",")
    backward_tejn_alignment[int(words[0])] = words[1].replace("/Users/Lena/Documents/Tibet_Orna/Tibet_data/Kangyur_Tenjur-CLEAN/enum_stem-flat/", "").replace("(USE OF THIS SECTION REQUIRES COMPLETING AN AUTHORIZATION FORM From ACIP)", "")



# #test:
# test_input = "2488, 684,279,290,R'A DZ'A Y'A TA TH'A GA T'A YA AARHA TE SAMYAKSAm ,3649,3662,TE DZO R'A DZ'A YA TA TH'A GA T'A YA AARHA TE SAMYAKSAm ,1.2289670483757886"
# print shared.create_match_from_merged_string(test_input)


def shrink_data(data, min_rows, min_columns):
    # check vector
    relevant_column_indices = []
    for col_index in xrange(len(data[0])):
        cnt = 0
        max_val = 0
        min_val = 80000
        for row_index in xrange(len(data)):
            if list_of_lists[row_index][col_index] != 0:
                max_val = max(max_val, data[row_index][col_index])
                min_val = min(min_val, data[row_index][col_index])
                cnt += 1
        if cnt < min_columns:
            continue
        else:
            # print i, "min", min_val, "max", max_val
            relevant_column_indices.append(col_index)

    relevant_row_indices = []
    for i in xrange(len(data)):
        cnt = 0
        for j in xrange(len(data[0])):
            if list_of_lists[i][j] != 0:
                cnt += 1
        if cnt < min_rows:
            continue
        else:
            relevant_row_indices.append(i)
    # leave only significant indices
    list_of_lists_filtered = []
    row_new_to_real_mapping = {}

    new_row_index = 0
    for row_index in xrange(len(data)):
        if row_index in relevant_row_indices:
            new_list = []
            for index in relevant_column_indices:
                new_list.append(data[row_index][index])
            list_of_lists_filtered.append(new_list)
            row_new_to_real_mapping[new_row_index] = row_index
            new_row_index += 1

    return list_of_lists_filtered, row_new_to_real_mapping

all_merged_files = shared.load_merged_file_names(shared.merged_results_folder)
files_to_vec_map = {}
indices_to_file_names = []
list_of_lists = []


for f in all_merged_files:
    res = shared.read_merged_matches_file(f)
    d = [0. for i in xrange(vector_size)]
    for r in res:
        d[int(r.citation2.source)] = d[int(r.citation2.source)] + 1
    files_to_vec_map[f] = d
    indices_to_file_names.append(r.citation1.source)
    list_of_lists.append(d)

#### test:
#list_of_lists = [[0,0,0,0,0], [0,1,0,0,0], [1,2,0,0,1], [0, 0, 0, 3, 0], [2,5,1,2,0], [1,2,3,4,0]]

list_of_lists_filtered, row_index_mapping= shrink_data(list_of_lists, min_rows=1, min_columns=2)
print "after shrinking, matrix dimension: ",len(list_of_lists_filtered), ",", len(list_of_lists_filtered[0])
input_for_cluster = np.array(list_of_lists_filtered)
print "matrix shape:", input_for_cluster.shape


def cosine_sim(matrix, row_index_mapping):
    A = matrix
    A_sparse = sparse.csr_matrix(A)

    similarities = cosine_similarity(A_sparse)
    print('pairwise dense output:\n {}\n'.format(similarities)[:10])

    # also can output sparse matrices
    similarities_sparse = cosine_similarity(A_sparse, dense_output=False)
    print "______"
    res = {}
    cx = scipy.sparse.coo_matrix(similarities_sparse)
    for i, j, v in zip(cx.row, cx.col, cx.data):
        res[(i,j)] = v
    #print res
    print "________"
    #print type(similarities_sparse[0])
    sorted_sparse = sorted(res.items(), key=lambda x: x[1], reverse=True)
    print len(sorted_sparse), len(matrix)
    ### print to 10 results ###
    num_to_return = 10
    return_value = []
    for item in sorted_sparse:
        if item[0][0] != item[0][1]:
            return_value.append(item)
            num_to_return -= 1
        if num_to_return <= 0:
            break
    return return_value
    # print('pairwise sparse output:\n {}\n'.format(sorted_sparse[len(matrix)-1:len(matrix) + 5]))

cosine_res = cosine_sim(input_for_cluster, row_index_mapping)
i = 0
for item in cosine_res:
    f1 = item[0][0]
    f2 = item[0][1]
    real_f1 = row_index_mapping[f1]
    real_f2 = row_index_mapping[f2]
    print i, backward_tejn_alignment.get(int(indices_to_file_names[real_f1].split("/")[-1])), "/", backward_tejn_alignment.get(int(indices_to_file_names[real_f2].split("/")[-1]))
    "------------------------------------------------------------------------------"
    i += 1

######################### only a test sample ###########################
"""
# data generation
data = vstack((rand(150,2) + array([.5,.5]),rand(150,2)))
#print data
data = input_for_cluster

# computing K-Means with K = 2 (2 clusters)
centroids,labeles = kmeans2(data,3, minit='points')
# assign each sample to a cluster
idx,_ = vq(data,centroids)

# some plotting using numpy's logical indexing
plot(data[idx==0,0],data[idx==0,1],'ob',
     data[idx==1,0],data[idx==1,1],'or',
     data[idx==2,0],data[idx==2,1],'og') # third cluster points
plot(centroids[:,0],centroids[:,1],'sm',markersize=8)
show()
"""
#########################################################################






