#!/usr/bin/python
# _*_ coding: UTF-8 _*_

import sys
reload(sys) 
sys.setdefaultencoding('utf-8')
import time
import urllib2  
import re  
import time

str1 = ""
str2 = ""
url = 'http://tieba.baidu.com/p/2751660846' 

find_re = re.compile(r'1980204171(.+?)1980208016', re.DOTALL)  
html = urllib2.urlopen(url).read().decode('gb2312', 'ignore').encode('utf-8')  
for x in find_re.findall(html):  
	str2 = x[0].decode('gb2312', 'ignore').encode('utf-8')
	print str2
print 'Done!'
