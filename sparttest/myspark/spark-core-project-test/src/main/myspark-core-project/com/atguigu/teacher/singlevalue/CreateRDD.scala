package com.atguigu.teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 09:30
  */
object CreateRDD {
    
    
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("CreateRDD").setMaster("local[2]")
        val sc: SparkContext = new SparkContext(conf)
        sc.setLogLevel("error")
        // 1. 通过标准的scala集合来得到
        //        val list1: List[Int] = List(30, 50, 70, 60, 10, 20)
        //        val rdd: RDD[Int] = sc.parallelize(list1)
        //        val rdd = sc.makeRDD(list1)
        //        rdd.collect().foreach(println)
        // 2. 通过外部存储
        //val linesRDD = sc.textFile("")
        
        sc.stop()
    }
}

/*
得到RDD 主要3中途径:

1. 通过标准的scala集合来得到
2. 从外部存储读取得到
3. 从其他RDD转换得到

 */
