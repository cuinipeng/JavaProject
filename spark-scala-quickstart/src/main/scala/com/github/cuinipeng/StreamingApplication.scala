package com.github.cuinipeng

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}

object StreamingApplication {

    private val logger: Logger = LoggerFactory.getLogger(StreamingApplication.getClass)

    def main(args: Array[String]): Unit ={
        if (args.length < 2) {
            System.err.println("Usage: com.github.cuinipeng.StreamingApplication <hostname> <port>")
            System.exit(1)
        }

        SparkLogUtil.setStreamingLogLevels()

        val ssc = getSparkStreamingContext()
        val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_AND_DISK_SER)
        val words = lines.flatMap(_.split(" "))
        val wordCounts = words.map(x => (x, 1)).reduceByKey(_+_)
        wordCounts.print(10)
        ssc.start()
        ssc.awaitTermination()

    }

    def getSparkStreamingContext(): StreamingContext = {
        // 创建 Streaming 上下文, 批处理间隔为 1s
        val appName = "NetworkWordCountApplication"
        val conf = new SparkConf().setAppName(appName)
        val ssc = new StreamingContext(conf, Seconds(1))

        return ssc
    }
}
