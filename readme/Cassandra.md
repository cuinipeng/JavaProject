http://cassandra.apache.org/
https://www.ibm.com/developerworks/cn/opensource/os-cn-cassandra/index.html

### Cassandra 的数据存储结构
* Cassandra 的数据模型是基于列族(Column Family)的四维或五维模型.
* 采用 Memtable 和 SSTable 的方式进行存储
* 在 Cassandra 写入数据之前, 需要先记录日志(CommitLog), 然后数据开始写入到 Column Family 对应的 Memtable 中,Memtable 是一种按照 key 排序数据的内存结构, 在满足一定条件时, 再把 Memtable 的数据批量的刷新到磁盘上, 存储为 SSTable .
* Cluster/Keyspace/ColumnFamily(SuperColumn )/Column
