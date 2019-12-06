package wordcounttest.day02

import org.apache.spark.{SparkConf, SparkContext}

object RDDdistinct {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 10, 20,30,10),4)

    val rdd2=rdd1.distinct(2)
    println(rdd2.collect().mkString(","))


  }
}
