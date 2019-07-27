### HBase Reference
http://hbase.apache.org/book.html

### HBase Java API
<dependency>
  <groupId>org.apache.hbase</groupId>
  <artifactId>hbase-shaded-client</artifactId>
  <version>2.2.0</version>
</dependency>

Configuration config = HBaseConfiguration.create();
config.set("hbase.zookeeper.quorum", "localhost"); 

### HBase Shell
	The Apache HBase Shell is (J)Ruby's IRB with some HBase
particular commands added. Anything you can do in IRB, you
should be able to do in the HBase Shell.

$ hbase shell


### Scripting with Ruby
$ hbase org.jruby.Main PATH_TO_SCRIPT