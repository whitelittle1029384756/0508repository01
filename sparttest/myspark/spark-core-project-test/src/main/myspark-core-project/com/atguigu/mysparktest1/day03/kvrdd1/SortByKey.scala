package com.atguigu.mysparktest1.day03.kvrdd1

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 13:49
  */
object SortByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd = sc.parallelize(Array((1, "a"), (10, "b"), (11, "c"), (4, "d"), (20, "d"), (10, "e")))
        var rdd2 = rdd.sortByKey(false)
//        rdd2.pipe()
        rdd2.collect.foreach(println)

        sc.stop()
        
    }
}
/*
sortBy(x => x...)

sortByKey
    按照key进行排序. 要求key能够排序
    
    都是整体排序.
 */
