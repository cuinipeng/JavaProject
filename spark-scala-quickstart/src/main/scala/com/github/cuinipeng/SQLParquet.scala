package com.github.cuinipeng

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.slf4j.{Logger, LoggerFactory}

import scala.util.Properties

object SQLParquet {

    private val logger: Logger = LoggerFactory.getLogger(SQLParquet.getClass)

    def main(args: Array[String]): Unit = {
        val sparkHome = Properties.envOrElse("SPARK_HOME", "/opt/spark/2.4.3")
        val jsonFile = "file://" + sparkHome + "/examples/src/main/resources/people.json"
        val parquetFile = "file://" + sparkHome + "/examples/src/main/resources/users.parquet"
        val tmpParquetFile = "file:///tmp/people.parquet"

        val sc = getSparkContext()

        readParquet(sc, parquetFile)
        writeParquet(sc, jsonFile, tmpParquetFile)

        closeSparkContext(sc)
    }

    def getSparkContext(): SparkContext = {
        val appName = "Simple Application"
        // val master = "local[4]"
        // val conf = new SparkConf().setAppName(appName).setMaster(master)
        val conf = new SparkConf().setAppName(appName)
        val sc = new SparkContext(conf)

        return sc
    }

    def closeSparkContext(sc: SparkContext): Unit = {
        sc.stop()
    }

    def readParquet(sc: SparkContext, filename: String): Unit = {
        // 读取 Parquet 文件, 转换为 DataFrame
        val tableName = "usersTempTab"
        val sqlContext = new SQLContext(sc)

        val users = sqlContext.read.load(filename)
        users.registerTempTable(tableName)
        val usersRDD = sqlContext.sql("select * from " + tableName).rdd
        usersRDD.foreach(row => println("name: " + row(0) + ", favorite color: " + row(1)))
    }

    def writeParquet(sc: SparkContext, inFilename: String, outFilename: String): Unit = {
        // 转换 json 文件为 Parquet 格式
        val tableName = "usersTempTab"
        val sqlContext = new SQLContext(sc)

        val df = sqlContext.read.json(inFilename)
        df.select("name", "age").write.format("parquet").save(outFilename)
    }
}
