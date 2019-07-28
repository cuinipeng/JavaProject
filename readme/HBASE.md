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

### HBase Shell �ǽ���ģʽ��-n or --non-interactive��
```shell
$ ehco "describe 'test'" | hbase shell -n 2> /dev/null
```

### ���ļ��ж�ȡHBase Shell����
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

### ����VMѡ���HBase Shell
```shell
$ HBASE_SHELL_OPTS="-verbose:gc -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps \
-XX:+PrintGCDetails -Xloggc:$HBASE_HOME/logs/gc-hbase.log" hbase shell
```

### ��ѯHBase Shell����
```shell
hbase(main):001:0> @shell.hbase.configuration.get("hbase.zookeeper.quorum")
hbase(main):002:0> @shell.hbase.configuration.get("hbase.rpc.timeout")
hbase(main):003:0> @shell.hbase.configuration.setInt("hbase.rpc.timeout", 61010)
```

### irbrc ���� HBase Shell
```
$ more ~/.irbrc
require 'irb/ext/save-history'
IRB.conf[:SAVE_HISTORY] = 100
IRB.conf[:HISTORY_FILE] = "#{ENV['HOME']}/.irb-save-history"
IRB.conf[:ECHO] = false
```

### Ԥ�ȷ��ѱ�
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

### ��Ч�� count
```shell
hbase(main):001:0> count '<tablename>', CACHE => 1000
```

kubernetes����kubeadm���ھ���Դ��
https://my.oschina.net/Kanonpy/blog/3006129
kubernetes��װ�����ڻ�����
https://zhuanlan.zhihu.com/p/46341911
Kibana �û�ָ�ϣ��������Լ����Ǳ��̣�
https://segmentfault.com/a/1190000015140923
�ڹ���ʹ��kubeadm�k8s��Ⱥ
https://www.jianshu.com/p/c138e97423a4
�ְ��ֽ���һ�� Elasticsearch ��Ⱥ
https://juejin.im/post/5bad9520f265da0afe62ed95
Hibernate �̳�
https://www.w3cschool.cn/hibernate/skzl1idz.html
Java H2 tutorial
http://zetcode.com/java/h2database/