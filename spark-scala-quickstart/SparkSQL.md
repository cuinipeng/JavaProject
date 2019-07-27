https://spark.apache.org/docs/latest/sql-data-sources.html

### 数据源
1. 默认数据源: parquet (spark.sql.sources.default)
2. 手动指定数据源
   val df = spark.read.format("json").load(jsonFile)
   df.select("field1", "field2").write.format("parquet").save(parquetFile)
3. 指定数据源加载属性
   val df = spark.read.format("csv")
       .option("sep", ";")
       .option("inferSchema", "true")
       .option("header", "true")
       .load(csvFile)
4. Parquet 文件是自描述文件格式, 读写和保存 parquet
   val spark = SparkSession.builder().appName(appName).getOrCreate()
   import spark.implicits._
   val df1 = spark.read.json(jsonFile)
   df1.write.parquet("data.parquet")
   val df2 = spark.read.parquet("data.parquet")
   df2.createOrReplaceTempView("viewName")
   val df3 = spark.sql("SELECT field FROM viewName WHERE field BETWEEN num1 AND num2")
   df3.map(attributes => "Name: " + attributes(0)).show(100)
5. 文本转化为 Parquet 格式
    Spark SQL 提供了对读取和写入 Parquet 文件的支持, 能够自动保留原始数据的模式.
    Parquet 模式通过 Data Frame API, 使数据文件对 Spark SQL 应用程序 "不言自明"

    def convert(spark: SparkSession, filename: String, schema: StructType, tablename: String) {
        // import text-based table first into a data frame
        val df = spark.read.format("csv")
            .schema(schema).option("delimiter", "|")
            .load(filename)
        df.write.parquet("/usr/spark/data/parquet/" + tableName)
    }

    schema= StructType(Array(
        StructField("cp_catalog_page_sk",        IntegerType,false),
        StructField("cp_catalog_page_id",        StringType,false),
        StructField("cp_start_date_sk",          IntegerType,true),
        StructField("cp_end_date_sk",            IntegerType,true),
        StructField("cp_department",             StringType,true),
        StructField("cp_catalog_number",         LongType,true),
        StructField("cp_catalog_page_number",    LongType,true),
        StructField("cp_description",            StringType,true),
        StructField("cp_type",                   StringType,true)))

    val spark = SparkSession.builder()
        .appName(appName)
        .config("spark.sql.parquet.compression.codec", "gzip")
        .getOrCreate()
    // 默认启用 gzip 压缩
    convert(spark, hdfsPath +"/catalog_page/*", schema, "catalog_page")