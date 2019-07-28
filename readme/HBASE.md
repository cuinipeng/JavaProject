### HBase Reference
http://hbase.apache.org/book.html

### HBase Java API
```pom.xml
<dependency>
  <groupId>org.apache.hbase</groupId>
  <artifactId>hbase-shaded-client</artifactId>
  <version>2.2.0</version>
</dependency>
```
```java
Configuration config = HBaseConfiguration.create();
config.set("hbase.zookeeper.quorum", "localhost"); 
```

### HBase Shell
    The Apache HBase Shell is (J)Ruby's IRB with some HBase
	particular commands added. Anything you can do in IRB, you
	should be able to do in the HBase Shell.
```shell
$ hbase shell
```


### Scripting with Ruby
```shell
$ hbase org.jruby.Main PATH_TO_SCRIPT
```

### HBase Shell 非交互模式（-n or --non-interactive）
```shell
$ ehco "describe 'test'" | hbase shell -n 2> /dev/null
```

### 从文件中读取HBase Shell命令
```shell
$ cat sample_comands.txt                                                                                                                   
create 'test', 'cf'
list 'test'
put 'test', 'row1', 'cf:a', 'value1'
put 'test', 'row2', 'cf:b', 'value2'
put 'test', 'row3', 'cf:c', 'value3'
put 'test', 'row4', 'cf:d', 'value4'
scan 'test'
get 'test', 'row1'
disable 'test'
enable 'test'
disable 'test'
drop 'test'

tables = list('CLS_SPARK_.*')
tables.map { |table| disable table; drop table }

exit

$ hbase shell ./sample_comands.txt
```

### 传递VM选项给HBase Shell
```shell
$ HBASE_SHELL_OPTS="-verbose:gc -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps \
-XX:+PrintGCDetails -Xloggc:$HBASE_HOME/logs/gc-hbase.log" hbase shell
```

### 查询HBase Shell配置
```shell
hbase(main):001:0> @shell.hbase.configuration.get("hbase.zookeeper.quorum")
hbase(main):002:0> @shell.hbase.configuration.get("hbase.rpc.timeout")
hbase(main):003:0> @shell.hbase.configuration.setInt("hbase.rpc.timeout", 61010)
```

### irbrc 配置 HBase Shell
```
$ more ~/.irbrc
require 'irb/ext/save-history'
IRB.conf[:SAVE_HISTORY] = 100
IRB.conf[:HISTORY_FILE] = "#{ENV['HOME']}/.irb-save-history"
IRB.conf[:ECHO] = false
```

### 预先分裂表
```shell
hbase(main):001:0> create 'test1', 'cf', SPLITS => ['10','20','30']
hbase(main):002:0> create 'test2', 'cf', SPLITS_FILE => 'splits.txt'
hbase(main):003:0> create 'test3','cf', { NUMREGIONS => 4, SPLITALGO => 'UniformSplit' }
hbase(main):004:0> create 'test4','f1', { NUMREGIONS => 5, SPLITALGO => 'HexStringSplit' }
```

### Debug
```shell
$ hbase shell -d
hbase(main):001:0> debug
```

### 高效的 count
```shell
hbase(main):001:0> count '<tablename>', CACHE => 1000
```

kubernetes部署（kubeadm国内镜像源）
https://my.oschina.net/Kanonpy/blog/3006129
kubernetes安装（国内环境）
https://zhuanlan.zhihu.com/p/46341911
Kibana 用户指南（构建你自己的仪表盘）
https://segmentfault.com/a/1190000015140923
在国内使用kubeadm搭建k8s集群
https://www.jianshu.com/p/c138e97423a4
手把手教你搭建一个 Elasticsearch 集群
https://juejin.im/post/5bad9520f265da0afe62ed95
Hibernate 教程
https://www.w3cschool.cn/hibernate/skzl1idz.html
Java H2 tutorial
http://zetcode.com/java/h2database/