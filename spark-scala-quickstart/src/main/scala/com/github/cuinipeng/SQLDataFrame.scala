package com.github.cuinipeng

import org.apache.spark.sql.{DataFrame, Encoders, Row, SparkSession}
import org.apache.spark.sql.types._
import org.slf4j.{Logger, LoggerFactory}

import scala.util.Properties

case class Person(name: String, age: Long)

object SQLDataFrame {

    private val logger: Logger = LoggerFactory.getLogger(SQLDataFrame.getClass)
    private val max_num: Int = 10;

    def main(args: Array[String]): Unit = {
        val sparkHome = Properties.envOrElse("SPARK_HOME", "/opt/spark/2.4.3")
        val jsonFile = "file://" + sparkHome + "/examples/src/main/resources/people.json"

        val spark: SparkSession = setupSparkSession()

        testUntypedDatasetOperations(spark, jsonFile)
        testSQLQueriesProgrammatically(spark, jsonFile)
        testDatasets(spark, jsonFile)

        teardownSparkSession(spark)
    }

    def setupSparkSession(): SparkSession = {
        val appName = "Simple SQL Basic Example"
        val spark = SparkSession.builder().appName(appName)
            .config("spark.config.option", "value")
            .getOrCreate()
        return spark
    }

    def teardownSparkSession(spark: SparkSession): Unit = {
        spark.close()
    }

    def testUntypedDatasetOperations(spark: SparkSession, jsonFile: String): Unit = {
        // 创建 DataFrame
        val df = spark.read.json(jsonFile)

        // 显示 DataFrame 前 10 行内容
        df.show(max_num)

        // 打印 schema
        df.printSchema()

        // 打印某一列
        df.select("name").show(max_num)

        import spark.implicits._

        // 年龄 +1
        df.select($"name", $"age" + 1).show(max_num)

        // 选择年龄大于 21
        df.select($"age" > 21).show(max_num)

        // 根据年龄分组计算
        df.groupBy("age").count().show(max_num)
    }

    def testSQLQueriesProgrammatically(spark: SparkSession, jsonFile: String): Unit = {
        val globalView = true
        var viewName = "people"
        val df = spark.read.json(jsonFile)
        var sqlDf: DataFrame = null

        if (globalView == true) {
            /* Register the DataFrame as a global temporary view */
            df.createGlobalTempView(viewName)
            viewName = "global_temp." + viewName
            // Global temporary view is cross-session
            sqlDf = spark.newSession().sql("SELECT * FROM " + viewName)
        } else {
            /***
             * Temporary views in Spark SQL are session-scoped and will disappear
             * if the session that creates it terminates.
             */
            df.createOrReplaceTempView(viewName)
            sqlDf = spark.sql("SELECT * FROM " + viewName)
        }

        sqlDf.show(max_num)
    }

    def testDatasets(spark: SparkSession, jsonFile: String): Unit = {
        // 为了 RDD 到 DataFrame 的隐式转换导入
        import spark.implicits._
        var viewName = "people"

        val peopleDS = spark.read.json(jsonFile).as[Person]
        peopleDS.show(max_num)

        // Dataset(RDD) 与 DataFrame 互操作
        // Inferring(推断) the Schema Using Reflection
        val peopleDF = spark.read.textFile(jsonFile)
            .map(_.split(","))
            .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
            .toDF()
        // 注册 DataFrame 作为一个临时视图(和 SparkSession 绑定的)
        peopleDF.createOrReplaceTempView(viewName)
        // 执行 SQL 查询
        val sql = "SELECT name, age FROM " + viewName + " WHERE age BETWEEN 13 AND 19"
        val teenagerDF = spark.sql(sql)
        // 通过索引访问每一字段
        teenagerDF.map(teenager => "Name: " + teenager(0)).show(max_num)
        // 通过字段名访问
        teenagerDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show(max_num)
        // 也可以一次从每一行解析多个字段
        // 定义一个隐式参数
        implicit val mapEncoder = Encoders.kryo[Map[String, Any]]
        teenagerDF.map(teenager => teenager.getValuesMap(List("name", "age"))).collect()


        // 动态定义 Schema
        val peopleRDD = spark.read.textFile(jsonFile)
        val schemaString = "name age"
        // 生成 Schema
        val fields = schemaString.split(" ")
            .map(fieldName => StructField(fieldName, StringType, nullable = true))
        val schema = StructType(fields)
        // 转换 RDD 到 Rows
        val rowRDD = peopleRDD.map(_.split(","))
            .map(attributes => Row(attributes(0), attributes(1).trim))
    }
}
