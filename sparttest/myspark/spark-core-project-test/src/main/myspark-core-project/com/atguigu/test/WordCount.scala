package com.atguigu.test

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
      val conf= new SparkConf().setAppName("wordcount")
        .setMaster("local[*]")
    val sc = new SparkContext(conf)
     val tem= sc.textFile("C:\\Users\\lzp\\Desktop\\hello.txt")


  }

}
