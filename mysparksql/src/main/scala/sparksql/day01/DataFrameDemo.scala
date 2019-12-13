package sparksql.day01

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
object DataFrameDemo {
  def main(args: Array[String]): Unit = {
    // 初始化sparkSession
     val spark=SparkSession.builder()
      .master("local[2]")
         .appName("DataFrameDemo")
       .getOrCreate()
    //导入隐式转换
 //2.创建rdd
val rdd1 = spark.sparkContext.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))

spark.close()
}
}
