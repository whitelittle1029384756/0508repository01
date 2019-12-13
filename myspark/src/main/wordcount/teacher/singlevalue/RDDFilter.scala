package teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 10:05
  */
object RDDFilter {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(30, 5, 70, 6, 1, 2, 10, 3, 5, 7, 9), 2)
        /*val rdd2 = rdd1.filter(x => (x & 1) == 1)
        rdd2.collect.foreach(println)*/
        
        
        val rdd2 = rdd1
            .groupBy(x => x % 2 == 1)
            .map {
                case (k, it) => (k, it.sum)
            }
    
        rdd2.collect.foreach(println)
        sc.stop()
        
        
    }
}

