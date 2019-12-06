package teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 13:51
  */
object RDDSort {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 10, 20, 1, 3, 4, 5))
        val rdd2 = rdd1.sortBy(x => x, ascending = false)
        rdd2.collect.foreach(println)
        sc.stop()
        
    }
}
