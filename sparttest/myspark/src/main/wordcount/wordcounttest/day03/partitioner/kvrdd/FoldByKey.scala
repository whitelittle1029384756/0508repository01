package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 11:19
  */
object FoldByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
        val rdd2 = rdd1.map((_, 1))
        val rdd3: RDD[(String, Int)] = rdd2.foldByKey(0)(_ + _)
        rdd3.collect.foreach(println)
        sc.stop()
        
    }
}
