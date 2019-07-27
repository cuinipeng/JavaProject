#!/bin/bash
#
# Spark Streaming
#
WORK_DIR=$(dirname ${BASH_SOURCE})

# First run netcat in background
nohup nc -lk 9999 > /dev/null 2>&1 &

${SPARK_HOME}/bin/spark-submit \
    --class com.github.cuinipeng.StreamingApplication \
    --master local[4] \
    --executor-memory 4G \
    --total-executor-cores 4 \
    ${WORK_DIR}/../lib/spark-scala-quickstart-1.0.jar \
    localhost 9999
