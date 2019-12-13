package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 10:18
  */
object AgrrateByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        
        val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
        val rdd2 = rdd1.map((_, 1))
        val rdd3 = rdd2.aggregateByKey(0)(_ + _, _ + _)
        rdd3.collect.foreach(println)
        
        sc.stop()
        
    }
}
