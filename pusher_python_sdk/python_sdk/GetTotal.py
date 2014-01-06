#!/usr/bin/python
# _*_ coding: UTF-8 _*_

import sys
reload(sys) 
sys.setdefaultencoding('utf-8')
import time
import urllib2 
import re  
import os
#import pymongo
import time
from xml.etree.ElementTree import ElementTree,Element
#from Channel import *
#from pymongo import MongoClient

def prettyXml(element, indent, newline, level = 0): # elemnt为传进来的Elment类，参数indent用于缩进，newline用于换行    
    if element:  # 判断element是否有子元素    
        if element.text == None or element.text.isspace(): # 如果element的text没有内容    
            element.text = newline + indent * (level + 1)      
        else:    
            element.text = newline + indent * (level + 1) + element.text.strip() + newline + indent * (level + 1)    
    #else:  # 此处两行如果把注释去掉，Element的text也会另起一行    
        #element.text = newline + indent * (level + 1) + element.text.strip() + newline + indent * level    
    temp = list(element) # 将elemnt转成list    
    for subelement in temp:    
        if temp.index(subelement) < (len(temp) - 1): # 如果不是list的最后一个元素，说明下一个行是同级别元素的起始，缩进应一致    
            subelement.tail = newline + indent * (level + 1)    
        else:  # 如果是list的最后一个元素， 说明下一行是母元素的结束，缩进应该少一个    
            subelement.tail = newline + indent * level    
        prettyXml(subelement, indent, newline, level = level + 1) # 对子元素进行递归操作    

def read_xml(in_path):  
    '''''读取并解析xml文件 
       in_path: xml路径 
       return: ElementTree'''  
    tree = ElementTree()  
    tree.parse(in_path)  
    return tree  

def write_xml(tree, out_path):  
    '''''将xml文件写出 
       tree: xml树 
       out_path: 写出路径'''  
    tree.write(out_path, encoding="utf-8",xml_declaration=True)

def find_nodes(tree, path):  
    '''''查找某个路径匹配的所有节点 
       tree: xml树 
       path: 节点路径'''  
    return tree.findall(path) 
 
def add_child_node(nodelist, element):  
    nodelist.append(element)  

def create_node(tag, property_map, content):  
    '''''新造一个节点 
       tag:节点标签 
       property_map:属性及属性值map 
       content: 节点闭合标签里的文本内容 
       return 新节点'''  
    element = Element(tag, property_map)  
    element.text = content  
    return element  

baseurl = 'http://www.66721.com/32/32879/'
url0 = '6548404.html'
name = 1

#url0 = '3489544.html'
#name = 1

#client = MongoClient('ds039507.mongolab.com',39507)
#db = client.tangmen
#db.authenticate('jiangzhouq', 'biu1biu2biu3')


while True:
	global str102
	find_re = re.compile(r'<h1>(.+?)</h1>.+?<!--章节内容开始--><p data-plugin="keyword">(.+?)</p><!--章节内容结束-->.+?目录</a> &rarr; <a href="(.+?)">下一章'.encode('gb2312'), re.DOTALL)
	url = baseurl + url0
	html = urllib2.urlopen(url).read()  
	for x in find_re.findall(html): 
		str1 = x[0].decode('gb2312', 'ignore').encode('utf-8')
	#	str101 = str1.replace('第一集','').replace('第二集','').replace('第三集','').replace('第四集','').replace('第五集','');
	#	str102 = str101.replace('天梦冰蚕','',1).replace('怪物学院','').replace('武魂融合','').replace('黄金之路','').replace('冠军之战','').replace(' ','',2)
		str102 = str1.replace('正文 ','')
		str2 = x[2].decode('gb2312', 'ignore').encode('utf-8')
		url0 = str2
		str3 = x[1].decode('gb2312', 'ignore').encode('utf-8')
		str3 = str3.replace('<br />','')
		str3 = str3.replace('&nbsp;&nbsp;&nbsp;&nbsp;','\r\n')
		print str102 
		print url0
		print str3
		print os.getcwd() + '/xiaoshuo/' + str(name).zfill(5) + '.txt'
	if str102.count('第') == 0:
			continue
	f = open(os.getcwd() + '/xiaoshuo/' + str(name).zfill(5) + '.txt','w')
	f.write(str3)
	f.close()

	#1. 读取xml文件  
	tree = read_xml(os.getcwd() + "/test.xml")		
	#A. 找到父节点  
	nodes = tree.getroot()
	#A.新建节点  
	a = create_node("chapter", {"file":str(name).zfill(5),"name":str102,"time":str(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime()))},'')  
	#B.插入到父节点之下  
	add_child_node(nodes, a)
	prettyXml(nodes, '\t', '\n')
	write_xml(tree, os.getcwd() + "/test.xml")
	
	name = name + 1
	print 'Done!'
