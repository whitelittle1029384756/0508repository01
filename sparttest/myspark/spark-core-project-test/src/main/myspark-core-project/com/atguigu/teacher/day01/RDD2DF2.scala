package com.atguigu.teacher.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * Author lzc
  * Date 2019-09-20 14:13
  */
object RDD2DF2 {
    def main(args: Array[String]): Unit = {
        // 先初始化SparkSession
        val spark: SparkSession = SparkSession.builder()
            .master("local[2]")
            .appName("RDD2DF")
            .getOrCreate()
        val rdd2: RDD[(String, Int, String)] = spark.sparkContext.parallelize(Array(("lisi", 20, "male"), ("ww", 18, "female")))
        
        val rowRDD = rdd2.map {
            case (name, age, sex) => Row(name, age, sex)
        }
        //        val st: StructType = StructType(Array(StructField("name", StringType), StructField("age", IntegerType), StructField("sex", StringType)))
        val st: StructType =
            StructType(StructField("name", StringType) :: StructField("age", IntegerType) :: StructField("sex", StringType) :: Nil)
        
        val df: DataFrame = spark.createDataFrame(rowRDD, st)
        df.show
        spark.close()
    }
}


/*
rdd-df转换:
	
	rdd->df
		1. rdd.toDF(colName, colName,...)
		
		2. 数据封装样例类中, 然后存入rdd
			rdd.toDF
			样例类的属性, 自动成为字段名
		
		3. 直接通过api来转换
		    spark.createDataFrame(...)
		
	df->rdd
		df.rdd      RDD[Row]
 */