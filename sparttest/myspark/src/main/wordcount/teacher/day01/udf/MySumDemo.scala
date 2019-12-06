package teacher.day01.udf

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Author lzc
  * Date 2019-09-20 16:46
  */
object MySumDemo {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[*]")
            .appName("MySumDemo")
            .getOrCreate()
        import spark.implicits._
        import spark.sql
        // 1. 先注册
        spark.udf.register("mySum", new MySum)
        // 2. 使用
        val df: DataFrame = spark.read.json("c:/users.json")
        df.createOrReplaceTempView("user")
        sql("select mySum(age) sum from user").show
        
        spark.close()
        
    }
}
