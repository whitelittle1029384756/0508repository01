package com.atguigu.spark_sql.day01.udf

import org.apache.spark.sql.SparkSession

object MySumDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("MySumDemo")
      .getOrCreate()
    import spark.implicits._
        import spark.sql
    //先注册
    spark.udf.register("mySum",new MySum)
   //2.使用
    val df=spark.read.json("C:\\Users\\lzp\\Desktop\\users.json")
       df.createOrReplaceTempView("user")
        sql("select mySum(age) sum from user").show

         spark.close()
  }
}
