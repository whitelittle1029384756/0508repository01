package teacher.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
  * Author lzc
  * Date 2019-09-20 15:36
  */
object DS2Other1 {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[*]")
            .appName("DS2Other")
            .getOrCreate()
        import spark.implicits._
        val rdd = spark.sparkContext.parallelize(Array(User("lisi", 20, "male"), User("ww", 18, "female")))
        val df: DataFrame = rdd.toDF
        
        df.printSchema()  // 调试
        /*val ds: Dataset[User] = df.as[User]
        
        ds.show
        
        ds.toDF.show*/
    }
}

/*
rdd-df转换:
	
	rdd->df
		1. rdd.toDF(colName, colName,...)
		
		2. 数据封装样例类中, 然后存入rdd
			rdd.toDF
			样例类的属性, 自动成为字段名
		
	df->rdd
		df.rdd
		

rdd-ds转换
    rdd->ds
        首先定义一个样例类, 在rdd中存入样例类,
        rdd.toDS
    
    ds->rdd
        ds.rdd

df-ds转换
    df->ds
        先有样例类
        df.as[样例类]
    
    ds->df
        ds.toDF
    

 */