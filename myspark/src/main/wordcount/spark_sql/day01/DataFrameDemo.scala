package spark_sql.day01

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
  df.show(20)

spark.close()
}
}
