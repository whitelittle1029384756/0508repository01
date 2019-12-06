package com.atguigu.spark_sql.day01

import com.atguigu.teacher.day01.User
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object DataFrameDemo {
  def main(args: Array[String]): Unit = {
    // 初始化sparkSession
     val spark=SparkSession.builder()
      .master("local[2]")
         .appName("DataFrameDemo")
       .getOrCreate()
    //导入隐式转换
    import spark.implicits._
 //2.创建rdd
val rdd1 = spark.sparkContext.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
  val df= rdd1.toDF("name")
//  df.show(20)
    val rdd2 = spark.sparkContext.parallelize(Array(User("lisi", 20, "male"), User("ww", 18, "female")))
     val df2=rdd2.toDF
     df2.show
spark.close()
}
}
case class User(name:String,age:Int,sex:String)