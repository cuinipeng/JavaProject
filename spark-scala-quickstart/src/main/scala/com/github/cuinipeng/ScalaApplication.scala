package com.github.cuinipeng

import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source
import scala.reflect.io.File
import scala.util.Properties

//import scala.collection.immutable.HashMap


object ScalaApplication {

    private val logger: Logger = LoggerFactory.getLogger(ScalaApplication.getClass)

    def main(args: Array[String]): Unit = {
        // 读取环境变量
        // val sparkHome = sys.env("SPARK_HOME")
        val sparkHome = Properties.envOrElse("SPARK_HOME", "/opt/spark/2.4.3")
        val logFile = sparkHome + "/README.md"

        val appName = "Simple Application"
        // val master = "local[4]"
        // val conf = new SparkConf().setAppName(appName).setMaster(master)
        // val sc = new SparkContext(conf)
        val spark = SparkSession.builder().appName(appName).getOrCreate()

        if (!File(logFile).exists) {
            logger.warn(s"${logFile} is missing")
            spark.stop()
            return
        }

        // 日志打印要放在 SparkSession 之后
        logger.info(s"SPARK_HOME: ${sparkHome}")
        logger.info(s"logFile: ${logFile}")

        // logFile = "/my/directroy"
        // logFile = "/my/directroy/*.txt"
        // logFile = "/my/directroy/*.gz"
        val logData = spark.read.textFile(logFile).cache()

        val numAs = logData.filter(line => line.contains("a")).count()
        val numBs = logData.filter(line => line.contains("b")).count()

        logger.info(s"Lines with a: ${numAs}, Lines with b: ${numBs}")

        spark.stop()
    }

    def readFile(filename: String): Unit = {
        val source = Source.fromFile(filename)
        val lines = source.getLines()
        source.close()

        for (line <- lines) {
            println(line)
        }
    }
}
