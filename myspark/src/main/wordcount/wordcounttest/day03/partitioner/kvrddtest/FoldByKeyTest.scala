package wordcounttest.day03.partitioner.kvrddtest

import org.apache.spark.{SparkConf, SparkContext}

object FoldByKeyTest {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))

      val rdd3=rdd1.map((_,1)).foldByKey(0)(_+_)
    rdd3.collect.foreach(println)
    sc.stop()
  }
}
