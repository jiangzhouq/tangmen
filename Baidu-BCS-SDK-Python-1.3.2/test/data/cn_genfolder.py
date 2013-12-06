#!/usr/bin/python
#coding: utf-8

import os, sys, re
import random

usage = """
USAGE: 
    ./genfolder.py 3 2 xxx

will get:

$ tree xxx
xxx
|-- 0
|   |-- 0
|   `-- 1
|-- 1
|   |-- 0
|   `-- 1
`-- 2
    |-- 0
    `-- 1
"""

if len(sys.argv) != 4:
    print usage
    sys.exit()

a = int(sys.argv[1])
b = int(sys.argv[2])
target_dir = sys.argv[3]

def mkdirs(path):
    if not os.path.exists(path):
        os.makedirs(folder)

for i in range(a):
    folder = os.path.join(target_dir, str(i))
    mkdirs(folder)
    for j in range(b):
        f = os.path.join(folder, 'ÖÐÎÄ'+str(j))
        fd = open(f, 'w')
        fd.write(str(i*1000*1000 + j))
        fd.close()
