package com.atguigu.mysparkdstream.day07

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object RDDQueuedemo {
  def main(args: Array[String]): Unit = {
       val conf = new SparkConf().setAppName("RDDQueuedemo").setMaster("local[*]")
      val scc = new StreamingContext(conf,Seconds(5))
     val sc =scc.sparkContext
    //创建一个可变队列
    val queue=mutable.Queue[RDD[Int]]()

    val rddDS= scc.queueStream(queue,true)
    rddDS.reduce(_+_).print
    scc.start

    //循环的方式向队列中添加RDD
    for (elem<-1 to 5){
        queue+=sc.parallelize(1 to 100)
        Thread.sleep(2000)
    }
   scc.awaitTermination()
  }
}
