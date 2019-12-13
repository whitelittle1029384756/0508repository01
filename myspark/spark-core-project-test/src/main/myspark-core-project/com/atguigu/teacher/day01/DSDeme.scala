package com.atguigu.teacher.day01

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * Author lzc
  * Date 2019-09-20 15:18
  */
object DSDeme {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[*]")
            .appName("DSDeme")
            .getOrCreate()
        import spark.implicits._
        
        val arr1 = Seq(User("lisi", 20, "mael"), User("ww", 18, "mael"))
        var ds: Dataset[User] = arr1.toDS()
        ds.select("name").show
        
        // 1. 强类型
        ds.filter(_.age > 19).show
        // 2.弱类型
        ds.filter($"age" > 19).show  // df只能会用用这个
        
        
        /*ds.createOrReplaceTempView("user")
        val df = spark.sql("select * from user")*/
       
        spark.close()
        
    }
}


/*
ds 看成强类型
    user.name
    user.age...
df  row其实是弱类型
    row.getInt()....






 */