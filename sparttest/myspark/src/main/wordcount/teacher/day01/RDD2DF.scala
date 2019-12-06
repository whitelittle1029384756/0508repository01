package teacher.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * Author lzc
  * Date 2019-09-20 14:13
  */
object RDD2DF {
    def main(args: Array[String]): Unit = {
        // 先初始化SparkSession
        val spark: SparkSession = SparkSession.builder()
            .master("local[2]")
            .appName("RDD2DF")
            .getOrCreate()
        import spark.implicits._
        // 导入隐式转换
        // 2. 创建rdd
        /*val rdd1: RDD[String] = spark.sparkContext.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
//        val df = rdd1.toDF("word")
        val df = rdd1.toDF*/
        
//        val rdd2 = spark.sparkContext.parallelize(Array(("lisi", 20, "male"), ("ww", 18, "female")))
//        rdd2.toDF("name", "age", "sex").show
//        rdd2.toDF().show
        
        val rdd2 = spark.sparkContext.parallelize(Array(User("lisi", 20, "male"), User("ww", 18, "female")))
        val df: DataFrame = rdd2.toDF
        val rdd3: RDD[Row] = df.rdd
        rdd3.map(row => {
            row.getString(0)
        }).collect.foreach(println)
        
    }
}

case class User(name: String, age: Int, sex: String)

/*
rdd-df转换:
	
	rdd->df
		1. rdd.toDF(colName, colName,...)
		
		2. 数据封装样例类中, 然后存入rdd
			rdd.toDF
			样例类的属性, 自动成为字段名
		
	df->rdd
		df.rdd
 */