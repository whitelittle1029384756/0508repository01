package wordcounttest.day03.partitioner.kvrddtest

import org.apache.spark.{SparkConf, SparkContext}

object Sortbytest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(Array((1, "a"), (10, "b"), (11, "c"), (4, "d"), (20, "d"), (10, "e")))

    val rdd3=rdd.sortByKey(false)
//   val rdd4=        rdd3.sortBy(t=>t)((t1._1,t1._2)=>(Ordering.String.reverse,Ordering.Int.reverse)
    rdd3.collect.foreach(println)
    sc.stop()

  }
}
