package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 14:05
  */
object Join {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        var rdd1 = sc.parallelize(Array((1, "a"), (1, "b"), (2, "c"), (4, "xx")))
        val rdd2 = sc.parallelize(Array((1, "aa"), (3, "bb"), (2, "cc"), (2, "dd")))
        // 内连接
//        val rdd3: RDD[(Int, (String, String))] = rdd1.join(rdd2)
        // 左外
//        val rdd3 = rdd1.leftOuterJoin(rdd2)
//        val rdd3 = rdd1.rightOuterJoin(rdd2)
        // 全连接
        val rdd3 = rdd1.fullOuterJoin(rdd2)
        rdd3.collect.foreach(println)
        sc.stop()
        
    }
}
/*
a join b on a.x=b.x

 */
