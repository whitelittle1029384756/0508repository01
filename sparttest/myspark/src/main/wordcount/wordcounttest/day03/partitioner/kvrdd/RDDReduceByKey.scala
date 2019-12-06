package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 08:01
  */
object RDDReduceByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
        val wordOne = rdd1.map((_, 1))
//        val wordCount = wordOne.reduceByKey(_ + _)
        val wordCount = wordOne.reduceByKey(_ + _, 3)   // 预聚合
        wordCount.collect.foreach(println)
        sc.stop()
        
    }
}
