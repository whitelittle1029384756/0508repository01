package com.atguigu.mysparkdstream.day07

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyWordCount2 {
  def main(args: Array[String]): Unit = {
      val conf = new SparkConf().setAppName("MyWordCount2").setMaster("local[*]")

      val ssc= new StreamingContext(conf,Seconds(3))
      val  socketStream=ssc.socketTextStream("hadoop104",9999)
       val wordcount2=socketStream
           .flatMap(_.split(" "))
         .map((_,1))
         .reduceByKey(_+_)
      //最终处理
       wordcount2.print(100)
    //启动streamingContext
     ssc.start()

    ssc.awaitTermination()

  }
}
