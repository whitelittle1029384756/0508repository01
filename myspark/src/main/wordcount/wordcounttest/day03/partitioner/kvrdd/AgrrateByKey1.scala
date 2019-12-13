package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 10:18
  */
object AgrrateByKey1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        
        val rdd1 = sc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
        //        val rdd3 = rdd1.aggregateByKey(Int.MinValue)( (max, v) => max.max(v) , _ + _)
        //        val rdd3 = rdd1.aggregateByKey(Int.MinValue)( _.max(_) , _ + _)
        
        /*val rdd3 = rdd1.aggregateByKey((Int.MinValue, Int.MaxValue))(
            (maxMin, v) => (maxMin._1.max(v), maxMin._2.min(v)) ,
            (maxMin1, maxMin2) => (maxMin1._1 + maxMin2._1, maxMin1._2 + maxMin2._2)
        )*/
        /*val rdd3 = rdd1.aggregateByKey((Int.MinValue, Int.MaxValue))(
            {
                case ((max, min), v) => (max.max(v), min.min(v))
            },
            {
                case ((max1, min1), (max2, min2)) => (max1 + max2, min1 + min2)
            }
        )*/
        
        /*val rdd3 = rdd1.aggregateByKey((0, 0))(
            {
                case ((sum, count), v) => (sum + v, count + 1)
            },
            {
                case ((sum1, count1), (sum2, count2)) => (sum1+ sum2, count1 + count2)
            }
        ).map{
            case (k, (sum, count)) => (k, sum.toDouble / count)
        }*/
        
        val rdd3 = rdd1.aggregateByKey((0, 0))(
            {
                case ((sum, count), v) => (sum + v, count + 1)
            },
            {
                case ((sum1, count1), (sum2, count2)) => (sum1 + sum2, count1 + count2)
            }
        ).mapValues {
            case (sum, count) => sum.toDouble / count
        }
        
        
        rdd3.collect.foreach(println)
        
        sc.stop()
        
    }
    
}

// (b, (maxSum, minSum))
// (b, (sum, count)) => (b, avg)