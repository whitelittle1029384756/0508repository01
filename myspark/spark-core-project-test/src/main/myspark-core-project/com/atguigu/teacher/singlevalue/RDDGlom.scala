package com.atguigu.teacher.singlevalue

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 10:05
  */
object RDDGlom {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 10, 20), 4)
        
        val rdd2: RDD[Array[Int]] = rdd1.glom()
        rdd2.collect.foreach(x => println(x.mkString(", ")))
        
        sc.stop()
        
        
    }
}
