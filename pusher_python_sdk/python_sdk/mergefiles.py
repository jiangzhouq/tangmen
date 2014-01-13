#!/usr/bin/python
# _*_ coding: UTF-8 _*_

import sys
reload(sys) 
import os
sys.setdefaultencoding('utf-8')
i = 603
f = open(os.getcwd() + '/xiaoshuo/10003.txt','w')
while (i < 867):
	f1 = open(os.getcwd() + '/xiaoshuo/' + str(i).zfill(5)  + '.txt','r')
	f.write(f1.read() + '\r\n')
	f1.close()
	print str(i).zfill(5) + '.txt', ' merged'
	i = i + 1
f.close()
