#!/usr/bin/env python
#coding:utf8

import os
import logging
import pybcs 

#设置日志级别
pybcs.init_logging(logging.INFO)


# 请修改这里
AK = '7q6l1grGD7pYFoDG3pEKTxwE'           #请改为你的AK
SK = 'tpTQFy3lK6mKG1yBVGH3lBitwUsfmjhY'        #请改为你的SK
BUCKET='bc7q6l1grgd7pyfodg3pektxwembaas'



bcs = pybcs.BCS('http://bcs.duapp.com/', AK, SK, pybcs.PyCurlHTTPC)    #这里可以显式选择使用的HttpClient, 可以是:
                                                                        #HttplibHTTPC
                                                                        #PyCurlHTTPC
lst = bcs.list_buckets()
print '---------------- list of bucket : '
for b in lst:
    print b
print '---------------- list end'

#声明一个bucket
b = bcs.bucket(BUCKET)

#创建bucket (创建后需要在yun.baidu.com 手动调整quota, 否则无法上传下载)
#b.create()

print b.list_objects_raw(prefix='', start=0, limit=100)

#获取bucket acl, 内容是json
#print b.get_acl()['body']

#将bucket 设置为公有可读写
#b.make_public()

#声明一个object
for atext in os.listdir('/home/jiangzhouq/Documents/github/tangmen/pusher_python_sdk/python_sdk/xiaoshuo/'):
	print os.path.basename(atext)
	o = b.object('/' + os.path.basename(atext))
	o.put_file('/home/jiangzhouq/Documents/github/tangmen/pusher_python_sdk/python_sdk/xiaoshuo/' + os.path.basename(atext), headers={})
#	o.delete()
#在bcs 上删除.
