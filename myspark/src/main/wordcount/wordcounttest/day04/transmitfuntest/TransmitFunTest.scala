package wordcounttest.day04.transmitfuntest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TransmitFunTest {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SerDemo").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(Array("hello world", "hello atguigu", "atguigu", "hahah"), 2)
    val searcher = new Searcher("hello")
    val result: RDD[String] = searcher.getMatchedRDD1(rdd)
    result.collect.foreach(println)
  }
}
//需求: 在 RDD 中查找出来包含 query 子字符串的元素

// query 为需要查找的子字符串
class Searcher(val query: String) extends Serializable {
  // 判断 s 中是否包括子字符串 query
  def isMatch(s : String) ={
    s.contains(query)
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD1(rdd: RDD[String]) ={
    rdd.filter(isMatch)  //
//      rdd.filter(s =>true)
  }
  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD2(rdd: RDD[String]) ={
    rdd.filter(_.contains(query))
  }
}
