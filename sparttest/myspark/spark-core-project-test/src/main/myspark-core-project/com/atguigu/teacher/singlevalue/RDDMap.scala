package com.atguigu.teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 10:05
  */
object RDDMap {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array("hello world", "hello hello", "atguigu atguigu", "atguigu atguigu"))
        val rdd2 = rdd1.flatMap(line => line.split(" "))
        rdd2.collect.foreach(println)
        
        sc.stop()
        
    }
}
