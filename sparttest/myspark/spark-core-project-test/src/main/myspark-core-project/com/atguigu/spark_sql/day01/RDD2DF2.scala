package com.atguigu.spark_sql.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object RDD2DF2 {
  def main(args: Array[String]): Unit = {
    // 初始化sparkSession
    val spark=SparkSession.builder()
      .master("local[2]")
      .appName("DataFrameDemo")
      .getOrCreate()
    //导入隐式转换
    import spark.implicits._
    //2.创建rdd
//    val rdd1 = spark.sparkContext.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
//    val df= rdd1.toDF("name")
val rdd2: RDD[(String, Int, String)] = spark.sparkContext.parallelize(Array(("lisi", 20, "male"), ("ww", 18, "female")))
   val rowRDD=rdd2.map({
     case (name,age,sex)=>Row(name,age,sex)
   })
  val st= StructType(Array(StructField("name",StringType),StructField("age",IntegerType),StructField("sex",StringType)))
     val df=spark.createDataFrame(rowRDD,st)
      df.show()
      spark.close()

  }
}
