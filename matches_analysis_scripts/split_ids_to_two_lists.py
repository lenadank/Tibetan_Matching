import sys
import shared as shared
sub_dir = sys.argv[1]



files_map = shared.build_file_names_map(shared.id_map_file)
kang = []
tenj = []
for key, value in files_map.items():
    value_clean = value.replace("Kangyur_Tenjur-CLEAN", "").lower()
    if "082-015" in value_clean or "097-001" in value_clean or "098-002" in value_clean:
        kang.append(key)
    else:
        tenj.append(key)
    
print "kang:", len(kang), "tenj", len(tenj)
out_dir = "../../out/Tang_Kenj_Split/"
kang_out = open(out_dir + "first.ids", "w")
tenj_out = open(out_dir + "second.ids", "w")
for key in kang:
    kang_out.write(key)
    kang_out.write(",")
    kang_out.write(files_map.get(key))
    kang_out.write("\n")

for key in tenj:
    tenj_out.write(key)
    tenj_out.write(",")
    tenj_out.write(files_map.get(key))
    tenj_out.write("\n")

kang_out.close()
tenj_out.close()
