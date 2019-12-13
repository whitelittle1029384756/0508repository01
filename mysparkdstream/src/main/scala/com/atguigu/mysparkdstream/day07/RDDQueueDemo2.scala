package com.atguigu.mysparkdstream.day07

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object RDDQueueDemo2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDQueuedemo").setMaster("local[*]")
    val scc = new StreamingContext(conf,Seconds(3))
       val queuerdd=mutable.Queue[RDD[Int]]()
    //用queueStream往rdd中添加rdd  oneattime 一次处理一个或多个
      val resultDStream= scc.queueStream(queuerdd,false)
        .reduce(_+_)
   //要打印
    resultDStream.print
    scc.start()
      while(true){
        //往可变rdd中添加元素
        queuerdd.enqueue(scc.sparkContext.parallelize(1 to 100))
        Thread.sleep(1500)
      }
    scc.awaitTermination()

  }
}
