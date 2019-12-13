package com.atguigu.mysparkdstream.day08

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowsTest2{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WindowsTest").setMaster("local[*]")
    val scc = new StreamingContext(conf,Seconds(4))
             scc.checkpoint("hdfs://hadoop104:9000/spark/ch2window")
       val sourceDStream= scc.socketTextStream("hadoop104",9999)

        val result=sourceDStream
          .flatMap(_.split("\\W"))
          .map((_,1))
          .reduceByKeyAndWindow((_:Int)+(_:Int),(_:Int)-(_:Int),Seconds(12),Seconds(8),filterFunc = _._2>0)
              result.print(100)
        scc.start()
       scc.awaitTermination()
  }
}
