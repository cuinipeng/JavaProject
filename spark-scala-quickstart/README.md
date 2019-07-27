
<< IDEA + Scala + Spark + Maven >>

### Useful commands
// Copy all libraries and dependencies to the `target/dependency` folder
% mvn dependency:copy-dependencies
% mvn clean
// Compile, run tests, and create jar
% mvn pakcge

### URL
http://docs.scala-lang.org/tutorials/scala-with-maven.html
https://github.com/scala/scala-module-dependency-sample

### Download [spark-2.4.3-bin-hadoop2.7.tgz](https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.3/spark-2.4.3-bin-hadoop2.7.tgz)
Spark is pre-built with Scala 2.11 except version 2.4.2, which is pre-built with Scala 2.12.

### Download [scala-2.11.12.zip](https://downloads.lightbend.com/scala/2.11.12/scala-2.11.12.zip)

### Spark SQL(Parquet Files)
? https://spark.apache.org/docs/latest/sql-data-sources-parquet.html
* http://dblab.xmu.edu.cn/blog/1091-2/

### HDFS: Command and Java API
http://c.biancheng.net/view/3576.html
https://www.w3cschool.cn/hadoop/hadoop_hdfs_operations.html

### Apache HDFS Document
http://hadoop.apache.org/docs/r2.7.7/

/opt/spark/2.4.3/examples/src/main/resources/users.parquet

hdfs namenode -format


### HDFS External Datasets
1. SparkContext.wholeTextFiles => (filename, context)
2. SparkContext.SequenceFiles


### Jetbrains Plugin
http://plugins.jetbrains.com/

### CodeStyle
https://github.com/google/styleguide

### 操作技巧：将 Spark 中的文本转换为 Parquet 以提升性能
https://www.ibm.com/developerworks/cn/analytics/blog/ba-parquet-for-spark-sql/index.html

### 大数据博客集合
https://www.iteblog.com/

### Scala 教程
https://www.runoob.com/scala/scala-tutorial.html








