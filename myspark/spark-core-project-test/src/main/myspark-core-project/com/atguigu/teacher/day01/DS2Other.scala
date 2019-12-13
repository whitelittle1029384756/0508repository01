package com.atguigu.teacher.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Author lzc
  * Date 2019-09-20 15:36
  */
object DS2Other {
    def main(args: Array[String]): Unit = {
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[*]")
            .appName("DS2Other")
            .getOrCreate()
        import spark.implicits._
        val rdd = spark.sparkContext.parallelize(Array(User("lisi", 20, "male"), User("ww", 18, "female")))
        val ds = rdd.toDS
        
        val rdd1: RDD[User] = ds.rdd
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


 */