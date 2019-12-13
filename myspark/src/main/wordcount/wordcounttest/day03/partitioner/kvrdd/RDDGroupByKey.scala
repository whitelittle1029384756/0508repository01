package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 08:01
  */
object RDDGroupByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
        val rdd2 = rdd1.map((_, 1)).groupByKey
        val rdd3 = rdd2.map {
            case (key, valueIt) => (key, valueIt.sum)
        }
        rdd3.collect.foreach(println)
        sc.stop()
        
    }
}
/*
reduceByKey
    会有预聚合操作
    如何有聚合的逻辑, 优先选择这个

groupByKey
    仅仅分组, 不会有任何的预聚合
    
    

 */