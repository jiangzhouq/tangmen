bcsh a bcs command line toolkit
###############################

:date: 2012-03-06 22:11:33
:tags: all, 
:category: misc
:author: ning
:Last modified: 2013-05-03 11:26:56


.. contents:: Table of Contents

install 
=======


环境要求:
---------

python 2.5, 2.6, 2.7

NOTICE: 请勿使用 **3.x**


windows xp: (TODO: 安装步骤未验证)
----------------------------------

1. 安装setuptools
+++++++++++++++++

 下载 http://peak.telecommunity.com/dist/ez_setup.py  保存, 这个脚本将帮组我们安装 setuptools
 运行该脚本，将会安装setuptools

2. 安装 pycurl
++++++++++++++

pycurl 可选安装, 如果不安装pycurl, 将默认使用httplib 作为上传下载, 会把文件内容读到内存中, 所以性能较差.
如果文件较大，必须安装pycurl, 安装步骤如下:


根据自己的python 版本下载安装exe

python 2.5: http://pycurl.sourceforge.net/download/pycurl-ssl-7.18.2.win32-py2.5.exe

python 2.6: http://www.lfd.uci.edu/~gohlke/pythonlibs/vhw9tnto/pycurl-7.19.0.win32-py2.6.exe

python 2.7: http://www.lfd.uci.edu/~gohlke/pythonlibs/vhw9tnto/pycurl-7.19.0.win32-py2.7.exe


3. 安装 pybcs
+++++++++++++

下载安装包: 

http://developer.baidu.com/wiki/index.php?title=docs/cplat/stor/sdk

安装:
python setup.py install 

4.设置环境变量

假设你的python安装路径为: ``C:\Python25\``

请将 ``C:\Python25\Scripts`` 加入环境变量

ubuntu: 
-------

1. 安装setuptools
+++++++++++++++++

sudo apt-get install setuptools
sudo apt-get install python-pycurl

2. 安装pybcs
++++++++++++

下载安装包: 

http://developer.baidu.com/wiki/index.php?title=docs/cplat/stor/sdk

安装:
python setup.py install 

pybcs usage
===========

pybcs 是bcs的python客户端接口实现. 

1. 获取AK&SK
   开发者需要使用百度账号登录百度开发者中心注册成为百度开发者并创建应用，方可获取应用ID、对应的API Key及Secret Key等信息。具体信息，请参考《开发指南》的相关介绍。

2. 配置AK, SK, Bucket: 

   将获得的ak ,sk , bucket填入example.py 

3. 运行::
  
    ./example.py

bcsh.py usage 
=============

配置ak, sk
----------

从yun.baidu.com 获取ak, sk后，通过auth命令 配置bcsh::

    $ ./tools/bcsh.py auth --AK=YOUR_AK_HERE --SK=YOUR_SK_HERE -v
    INFO:root:Namespace(AK='YOUR_AK_HERE', BCS_HOST='http://bcs.duapp.com/', SK='YOUR_SK_HERE', op='auth', verbose=1)
    INFO:root:write ak[YOUR_AK_HERE] sk[YOUR_SK_HERE] to /home/ning/.bcsh
    INFO:root:get ak, sk from :/home/ning/.bcsh , ak[YOUR_AK_HERE], sk[YOUR_SK_HERE]

高级配置
--------

在ubuntu下, 配置信息保存在 ~/.bcsh 中::

    [bcs]
    bcs_host = http://bcs.duapp.com/
    sk = xxxxxxxxxxxxxxxxxxxx
    ak = fQACfHjnKOrj

可以直接修改该文件, 手动配置 host, ak, sk 等信息

在windows 系统下, 配置文件的位置可以通过用-v参数运行bcsh.py::

    $ bcsh.py -v ls
    INFO:root:Namespace(bcs_url=None, detail=False, op='ls', thread=10, verbose=1)
    INFO:root:get ak, sk from :/home/ning/.bcsh , ak[xxxxxxxxxxxx], sk[xxxxxxxxxxxxxxxxxxxx]


help
----
请看help ::

    $ ./tools/bcsh.py  -h
    usage: bcsh.py [-h] [-v] [--version] {upload,auth,cat,ls,download,rm} ...
    
    positional arguments:
      {upload,auth,cat,ls,download,rm}
        auth                auth ak, sk
        ls                  list all object start with remote_path
        upload              upload local file/dir to bcs
        download            download remote file(s) to local
        rm                  delete all object start with remote_path, just like `rm remote_path* -rf`
        cat                 cat remote file content
    optional arguments:
      -h, --help            show this help message and exit
      -v, --verbose         verbose
      --version             Print the version and exit

子命令help::

    $ ./tools/bcsh.py auth -h
    usage: bcsh.py auth [-h] [-v] [--AK AK] [--SK SK] [--BCS-HOST BCS_HOST]

某个子命令的help::

    optional arguments:
      -h, --help           show this help message and exit
      -v, --verbose        verbose
      --AK AK              ak of bcs.
      --SK SK              sk of bcs.
      --BCS-HOST BCS_HOST  set bcs host , "http://bcs.duapp.com/"

ls
--

不带路径的ls会列出所有的bucket::

    $ ./tools/bcsh.py ls
    http://bcs.duapp.com/bcs-test
    http://bcs.duapp.com/idning


根据前缀列目录::

    $ ./tools/bcsh.py ls http://bcs.duapp.com/idning/abc
    http://bcs.duapp.com/idning/abc
    http://bcs.duapp.com/idning/abc/artical_db.py
    http://bcs.duapp.com/idning/abc/common.py
    http://bcs.duapp.com/idning/abc/common.pyx
    http://bcs.duapp.com/idning/abc/common.pyx.part0
    http://bcs.duapp.com/idning/abc/common.pyx.part1
    http://bcs.duapp.com/idning/abc/common.pyx.part2
    http://bcs.duapp.com/idning/abc/daemon.py
    http://bcs.duapp.com/idning/abc/db.sql
    http://bcs.duapp.com/idning/abc/taskq.py
    http://bcs.duapp.com/idning/abc/translate.py
    http://bcs.duapp.com/idning/abc/wordpresslib.py

列出详细信息, 使用 **-l**  参数::

    $ ./tools/bcsh.py ls http://bcs.duapp.com/idning/pybcs/ -l
    drwx------ 0          2012-03-08 14:51:16 http://bcs.duapp.com/idning/pybcs/__init__.py
    drwx------ 0          2012-03-08 14:51:17 http://bcs.duapp.com/idning/pybcs/__init__.pyc
    drwx------ 0          2012-03-08 15:07:50 http://bcs.duapp.com/idning/pybcs/autodia.out.dia
    drwx------ 0          2012-03-08 14:51:16 http://bcs.duapp.com/idning/pybcs/bcs.py
    drwx------ 0          2012-03-08 14:51:16 http://bcs.duapp.com/idning/pybcs/bcs.pyc

upload
------
上传单个文件::

    ./tools/bcsh.py upload ./README.md http://bcs.duapp.com/idning/____README.md
    $ ./tools/bcsh.py ls http://bcs.duapp.com/idning/
    ...
    http://bcs.duapp.com/idning/abc/daemon.py
    http://bcs.duapp.com/idning/abc/db.sql
    http://bcs.duapp.com/idning/____README.md

上传目录:  使用 **-r** 参数::

    $ ./tools/bcsh.py upload ./docs/ http://bcs.duapp.com/idning/pybcs -r
    $ ./tools/bcsh.py ls http://bcs.duapp.com/idning/pybcs
    http://bcs.duapp.com/idning/pybcs/__init__.py
    http://bcs.duapp.com/idning/pybcs/__init__.pyc
    http://bcs.duapp.com/idning/pybcs/autodia.out.dia
    http://bcs.duapp.com/idning/pybcs/bcs.py
    http://bcs.duapp.com/idning/pybcs/bcs.pyc
    http://bcs.duapp.com/idning/pybcs/bcsh-usage.rst
    http://bcs.duapp.com/idning/pybcs/bucket.py
    http://bcs.duapp.com/idning/pybcs/bucket.pyc
    http://bcs.duapp.com/idning/pybcs/common.py
    http://bcs.duapp.com/idning/pybcs/common.pyc
    http://bcs.duapp.com/idning/pybcs/config.pyc
    http://bcs.duapp.com/idning/pybcs/httpc.py
    http://bcs.duapp.com/idning/pybcs/httpc.pyc
    http://bcs.duapp.com/idning/pybcs/object.py
    http://bcs.duapp.com/idning/pybcs/object.pyc

download
--------
下载单个文件::

    $ ./tools/bcsh.py download http://bcs.duapp.com/idning/pybcs/__init__.py /tmp/x

下载目录, 使用 **-r** 参数::

    $ ./tools/bcsh.py download http://bcs.duapp.com/idning/pybcs/ pybcs.tmp -r
    $ ls pybcs.tmp/
    autodia.out.dia  bcs.py   bucket.py   common.py   config.pyc  httpc.pyc    __init__.pyc  object.pyc
    bcsh-usage.rst   bcs.pyc  bucket.pyc  common.pyc  httpc.py    __init__.py  object.py

cat  (打印远程文件的内容)
-------------------------

使用方法::

    $ ./tools/bcsh.py cat http://bcs.duapp.com/idning/pybcs/__init__.py

rm 前缀方式删除
---------------
删除前缀为 xxxx 的所有文件, 因为bcs 是异步删除，有一定延时:: 

    $ ./tools/bcsh.py rm -v http://bcs.duapp.com/idning/pybcs/ 
    INFO:root:Namespace(dry_run=False, op='rm', recursive=False, remote_path='http://bcs.duapp.com/idning/pybcs/', verbose=1)
    INFO:root:get ak, sk from :/home/ning/.bcsh , ak[YOUR_AK_HERE], sk[YOUR_SK_HERE]
    [INFO] pycurl -X GET "http://bcs.duapp.com/idning/?sign=MBO:YOUR_AK_HERE:YTEcE3OIX7jezAUc9mktcUQuu9k%3D&start=0&prefix=%2Fpybcs%2F&limit=100" 
    [INFO] pycurl -X DELETE "http://bcs.duapp.com/idning/pybcs/__init__.py?sign=MBO:YOUR_AK_HERE:flw0%2BtCjl8MrkfKb9q5ARSpwmS8%3D" 
    ...
    [INFO] pycurl -X DELETE "http://bcs.duapp.com/idning/pybcs/__init__.pyc?sign=MBO:YOUR_AK_HERE:dNmVOn8NydcUej0btZMkEI50A6s%3D" 


问题定位
========

-v -vv
------
使用-v 和-vv 会打印和服务器端的详细交互信息

如 ::

    $ ./tools/bcsh.py rm -v http://bcs.duapp.com/idning/pybcs/ 
    INFO:root:Namespace(dry_run=False, op='rm', recursive=False, remote_path='http://bcs.duapp.com/idning/pybcs/', verbose=1)
    INFO:root:get ak, sk from :/home/ning/.bcsh , ak[YOUR_AK_HERE], sk[YOUR_SK_HERE]

