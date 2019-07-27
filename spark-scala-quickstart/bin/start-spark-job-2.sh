#!/bin/bash
#
# Spark SQL(Parquet Files)
#
WORK_DIR=$(dirname ${BASH_SOURCE})

${SPARK_HOME}/bin/spark-submit \
    --class com.github.cuinipeng.SQLParquet \
    --master local[4] \
    --executor-memory 4G \
    --total-executor-cores 4 \
    ${WORK_DIR}/../lib/spark-scala-quickstart-1.0.jar
