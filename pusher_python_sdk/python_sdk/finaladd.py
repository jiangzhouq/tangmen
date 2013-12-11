#!/usr/bin/python
# _*_ coding: UTF-8 _*_

import sys
reload(sys) 
sys.setdefaultencoding('utf-8')
import time
import urllib2  
import re  
import time
import sched,os,threading
from xml.etree.ElementTree import ElementTree,Element
import logging
import pybcs 
######################################################
cmergednum = 4
cmergedfile = 10301
######################################################
# 请修改这里
AK = '7q6l1grGD7pYFoDG3pEKTxwE'           #请改为你的AK
SK = 'tpTQFy3lK6mKG1yBVGH3lBitwUsfmjhY'        #请改为你的SK
BUCKET='bc7q6l1grgd7pyfodg3pektxwembaas'
bcs = pybcs.BCS('http://bcs.duapp.com/', AK, SK, pybcs.PyCurlHTTPC)
url = 'http://tieba.baidu.com/p/2744503599'
b = bcs.bucket(BUCKET)
 
def prettyXml(element, indent, newline, level = 0):    
    if element:  
        if element.text == None or element.text.isspace():
            element.text = newline + indent * (level + 1)      
        else:    
            element.text = newline + indent * (level + 1) + element.text.strip() + newline + indent * (level + 1)    
    temp = list(element)  
    for subelement in temp:    
        if temp.index(subelement) < (len(temp) - 1): 
            subelement.tail = newline + indent * (level + 1)    
            subelement.tail = newline + indent * level    
        prettyXml(subelement, indent, newline, level = level + 1)

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

def if_match(node, kv_map):  
    '''''判断某个节点是否包含所有传入参数属性 
       node: 节点 
       kv_map: 属性及属性值组成的map'''  
    for key in kv_map:  
        if node.get(key) != kv_map.get(key):  
            return False  
    return True  

def get_node_by_keyvalue(nodelist, kv_map):  
    '''''根据属性及属性值定位符合的节点，返回节点 
       nodelist: 节点列表 
       kv_map: 匹配属性及属性值map'''  
    result_nodes = []  
    for node in nodelist:  
        if if_match(node, kv_map):  
            result_nodes.append(node)  
    return result_nodes  

def add_child_node(nodelist, element):  
    nodelist.append(element)  

def find_nodes(tree, path):  
    '''''查找某个路径匹配的所有节点 
       tree: xml树 
       path: 节点路径'''  
    return tree.findall(path)

def create_node(tag, property_map, content):  
    '''''新造一个节点 
       tag:节点标签 
       property_map:属性及属性值map 
       content: 节点闭合标签里的文本内容 
       return 新节点'''  
    element = Element(tag, property_map)  
    element.text = content  
    return element 

def performDedail(urlll = url):
	find_re = re.compile(r'<h1 class="core_title_txt" title="(.+?)">.+?近二十章连载(.+?)未完待续.+?<div class="share_btn_wrapper">'.encode('gb2312'), re.DOTALL)  
	html = urllib2.urlopen(urlll).read()  
	for x in find_re.findall(html):  
		str1 = x[0].decode('gb2312', 'ignore').encode('utf-8')
		str2 = x[1].decode('gb2312', 'ignore').encode('utf-8')
		str2 = re.sub(r'(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?','',str2,0)
		str2 = str2.replace('><a href=""','').replace('target="_blank"','').replace('</a>','').replace('-','')
		num = re.search(r'[^：\s?br<> ]',str2).start()
		str2 = str2[num:len(str2)] + '未完待续)'
#		print str1, str2
		#1. 读取xml文件  
		tree = read_xml("./txt/test.xml")		
		#A. 找到父节点  
		root = tree.getroot()
		#找到single
		single = root.find('single')
		name = len(single.findall('chapter')) + 1
		#A.新建节点  
		a = create_node("chapter", {"file":str(name).zfill(5),"name":str1},str(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())))  
		#B.插入到父节点之下  
		add_child_node(single, a)
		#找到merges
		merges = root.find('merges')
		#找到num为4的merge
		merge = get_node_by_keyvalue(merges, {"num":str(cmergednum)})[0]
		#version+1
		newversion = int(merge.get('version')) + 1
		merge.set('version',str(newversion).zfill(5))
		#更新至……
		merge.set('status' ,'更新至:' + str1)
		#格式化并保存
		prettyXml(tree.getroot(), '\t', '\n')
		write_xml(tree, "./txt/test.xml")
		#新建单个章节txt
		str22 = str2
		f1 = open(os.getcwd() + '/txt/chapter/' + str(name).zfill(5) + '.txt','w')
		f1.write(str2)
		f1.close()
		#合并当前章节
		f2 = open(os.getcwd() + '/txt/merged/' + str(cmergedfile) + '.txt','a')
		f2.write(str22)
		f2.close()
		##上传单个章节
		o = b.object('/' + str(name).zfill(5) + '.txt')
		o.put_file(os.getcwd() + '/txt/chapter/' + str(name).zfill(5) + '.txt', headers={})
		##上传xml文件
		o = b.object('/test.xml')
		o.put_file(os.getcwd() + '/txt/test.xml', headers={})
		##上传merged总章
		o = b.object('/' + str(cmergedfile) + '.txt')
		o.put_file(os.getcwd() + '/txt/merged/' + str(cmergedfile) + '.txt', headers={})
	print 'new added:',str1
