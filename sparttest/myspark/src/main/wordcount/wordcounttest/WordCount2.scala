package wordcounttest

import org.apache.spark.{SparkConf, SparkContext}

object WordCount2 {
  def main(args: Array[String]): Unit = {
    //1.创建sparkconf对象，并设置APP名字  打包时不能写
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc=new SparkContext(conf)
    sc.setLogLevel("error")
    //2.创建sparkcontext对象
    val rdd=     sc.textFile("C:\\Users\\lzp\\Desktop\\conf2").flatMap(_.split("\\W+"))
      .map((_,1)).reduceByKey(_+_)
    //3.创建rdd并执行转换
    val result=  rdd.collect()
    result.foreach(println)
    println("-------------")
     result.foreach(println)
    //4.关闭连接
    sc.stop()
  }
}
