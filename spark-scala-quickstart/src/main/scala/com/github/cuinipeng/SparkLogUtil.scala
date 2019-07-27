package com.github.cuinipeng

import org.apache.log4j.{Level, Logger}
import org.apache.spark.internal.Logging

object SparkLogUtil extends Logging {
    /* 如果用户没有配置 log4j 则会设置默认合理的 spark streaming 日志级别 */
    def setStreamingLogLevels(): Unit = {
        val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
        if (!log4jInitialized) {
            logInfo("Setting log level to [WARN] for spark streaming example." +
                " To override add a custom log4j.properties to the classpath.")
            Logger.getRootLogger.setLevel(Level.WARN)
        }
    }
}
