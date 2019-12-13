package com.atguigu.spark_sql.day01

import org.apache.spark.sql.SparkSession

object DSDemo {
  def main(args: Array[String]): Unit = {

      val spark: SparkSession = SparkSession
        .builder()
        .master("local[*]")
        .appName("DSDeme")
        .getOrCreate()
      import spark.implicits._
   val arr1=List(User("lisi",20,"male"),User("ww",18,"male"))
         val ds=arr1.toDS()
          val ds2=arr1.toDF()

     val df= ds2.select("name")
    ds.show()
      df.show()

      spark.close()
    }
}
