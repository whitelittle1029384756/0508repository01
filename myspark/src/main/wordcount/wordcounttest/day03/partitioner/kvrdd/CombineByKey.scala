package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 11:19
  */
object CombineByKey {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        /*val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
        val rdd2 = rdd1.map((_, 1))
        val rdd3 = rdd2.combineByKey(
            v => v + 10,
            (last: Int, v:Int) => last + v,
            (v1: Int, v2:Int) => v1 + v2)*/
        
        val rdd1 = sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)),2)
        
        val rdd3 = rdd1.combineByKey(
            v => (v, 1),
            (sumCount: (Int, Int), v: Int) => (sumCount._1 + v, sumCount._2 + 1),
            (sumCount1: (Int, Int), sumCount2:(Int, Int)) => (sumCount1._1 + sumCount2._1,sumCount1._2 + sumCount2._2 )
        ).mapValues(sumCount => sumCount._1.toDouble / sumCount._2)
        
        rdd3.collect.foreach(println)
        sc.stop()
        
    }
}
