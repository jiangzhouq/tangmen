# pybcs 
## 说明
百度云存储(bcs) python sdk

相关文档请参见：
[bcs wiki](http://developer.baidu.com/wiki/index.php?title=docs/cplat/stor)
[云存储管理控制台](http://developer.baidu.com/console)

## Usage

1. 在“管理控制台”(服务管理/我的密钥) 创建应用，获取应用的ak及sk信息, 
2. 创建bucket，尝试在网页上上传成功
3. 将获得的ak ,sk , bucket填入example.py 
4. ./example.py


## implemented api:
bcs : 
    + list-bucket

bucket :
    + create
    + delete 
    + list obj
    + get acl 
    + set acl
    - enable logging

object: 
    + upload by put
    + upload by post
    + delete 
    + get
    + head
    + set meta
    + copy
    + get acl 
    + set acl
    
superfile(object):
    + create
    
client use pybcs:
    + bcs log


## wish-list: 
    * 支持超时 !!!!
    * 支持代理
    * 限速
    * https 支持
    * 如果用户没有装pycurl，应该可以使用httplib实现的httpclient

##运行测试


    export AK=你的AK
    export SK=你的SK
    cd test/data/ && make && cd ../.. #生成测试数据.
    python test/test_pybcs.py
    

# tools/bcsh
bcs 脚本上传, 下载工具

参考 docs/bcsh-usage.rst 

# release
update Makefile, setup.py REV
