#!/usr/bin/python
# _*_ coding: UTF-8 _*_

import sys
reload(sys) 
import os
sys.setdefaultencoding('utf-8')
i = 869
f = open(os.getcwd() + '/xiaoshuo/10301.txt','w')
while (i < 909):
	f1 = open(os.getcwd() + '/xiaoshuo/' + str(i).zfill(5)  + '.txt','r')
	f.write(f1.read())
	f1.close()
	print str(i).zfill(5) + '.txt', ' merged'
	i = i + 1
f.close()
