package com.atguigu.teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 13:51
  */
object RDDDistinct {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 30, 70, 10))
        val rdd2 = rdd1.distinct()
        rdd2.collect.foreach(println)
        sc.stop()
        
    }
}
