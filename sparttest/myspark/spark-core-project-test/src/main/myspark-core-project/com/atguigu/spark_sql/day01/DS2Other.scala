package com.atguigu.spark_sql.day01

import org.apache.spark.sql.SparkSession

object DS2Other {
  def main(args: Array[String]): Unit = {

      val spark: SparkSession = SparkSession
        .builder()
        .master("local[*]")
        .appName("DS2Other")
        .getOrCreate()
      import spark.implicits._
      val rdd = spark.sparkContext.parallelize(Array(User("lisi", 20, "male"), User("ww", 18, "female")))
       val df=rdd.toDF()
        val ds =df.as[User]

  }
}
