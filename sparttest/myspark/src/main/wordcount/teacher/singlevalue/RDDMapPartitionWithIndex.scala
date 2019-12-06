package teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 10:05
  */
object RDDMapPartitionWithIndex {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[3]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 10, 20), 2)
        val rdd2 = rdd1.mapPartitionsWithIndex((index, it) => it.map((index, _)))
        println(rdd2.collect.mkString(", "))
        
        sc.stop()
        
    }
}
/*
map 和

mapPartition
 


 */
