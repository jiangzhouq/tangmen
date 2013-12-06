gen.py 0 > 0.data
gen.py 1 > 1.data
gen.py 2 > 2.data

gen.py 1024 > 1024B.data
gen.py 1025 > 1025B.data

gen.py 10K > 10K.data
gen.py 100K > 100K.data
gen.py 200K > 200K.data

gen.py 600K > 600K.data
gen.py 999K > 999K.data

gen.py 262144 > 256K.data
gen.py 262143 > 256K-1.data
gen.py 262145 > 256K+1.data

gen.py 1M > 1M.data
gen.py 1048577 > 1M+1.data
gen.py 1048575 > 1M-1.data

gen.py 10M > 10M.data

gen.py 1024 > ÖĞÎÄ.data

genfolder.py 15 10 xxx.folder.data
cn_genfolder.py 3 2 cn.folder.data
cn_genfolder.py 1 1 cn2.folder.data

