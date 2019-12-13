package com.atguigu.mysparkdstream.day07.unstate

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TransformDemo {
  def main(args: Array[String]): Unit = {
     val conf =new SparkConf().setAppName("TransformDemo").setMaster("local[*]")
    val ssc = new StreamingContext(conf,Seconds(3))

      val socketStream=ssc.socketTextStream("hadoop104",9999)

    val resultDStream= socketStream.transform(rdd=>{
       rdd.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    })
  resultDStream.print()
    ssc.start()
    ssc.awaitTermination()

  }
}
