package com.atguigu.spark_sql.day01.udf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

class MyAvg extends UserDefinedAggregateFunction{
  val list=List((0D,0D))
  override def inputSchema: StructType = StructType(StructField("column", DoubleType):: Nil)

  override def bufferSchema: StructType = StructType(StructField("sum", DoubleType)::StructField("count",IntegerType):: Nil)

  override def dataType: DataType = DoubleType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0)=0D
     buffer(1)=0
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer(0)=buffer.getDouble(0)+input.getDouble(0)
      buffer(1)=buffer.getInt(1)+1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0)=buffer1.getDouble(0)+buffer2.getDouble(0)
      buffer1(1)=buffer1.getInt(1)+buffer2.getInt(1)
  }

  override def evaluate(buffer: Row): Double = buffer.getDouble(0)/buffer.getInt(1)
}
