#!/usr/bin/python                                                               
#coding:utf-8
#author : ning

import os
import sys,time
import hashlib
import random
import unittest
import logging

sys.path.insert(0, os.path.dirname(os.path.realpath(__file__)) + "/..")

import pybcs
#pybcs.init_logging(logging.ERROR, True, log_file_path=None)
pybcs.init_logging(logging.INFO, True, log_file_path=None)
#pybcs.init_logging(logging.DEBUG, True, log_file_path=None)

HOST = 'http://bs.baidu.com' 

AK = 'gNQ17x3aoMs8'
SK = 'HWmtE17aWcg8sK2fn4B1J3w8vMm'
BUCKET = 'p-59b805098a2c627624741539e70eb52e'
OBJECT = '/009cf012ece59d79afbf3e73d9af739a'

bcs1 = pybcs.BCS(HOST, AK, SK, pybcs.HttplibHTTPC)    
bcs2 = pybcs.BCS(HOST, AK, SK)    
for bcs in [bcs1, bcs2]:
    b = bcs.bucket(BUCKET)
    o = b.object(OBJECT)
    try:
        o.get_to_file('xxxxxxx', headers= {'Range': 'bytes=414449660-'})
    except Exception, e:
        print 'Exception:\n', e

    try:
        o.get(headers= {'Range': 'bytes=414449660-'})
    except Exception, e:
        print 'Exception:\n', e

