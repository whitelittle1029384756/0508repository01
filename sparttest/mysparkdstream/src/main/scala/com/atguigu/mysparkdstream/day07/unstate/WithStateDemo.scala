package com.atguigu.mysparkdstream.day07.unstate

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WithStateDemo {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("TransformDemo").setMaster("local[*]")
    val ssc = new StreamingContext(conf,Seconds(3))
      ssc.checkpoint("hdfs://hadoop104:9000/spark/ck2")
      val socketStream=ssc.socketTextStream("hadoop104",9999)
      val resultDSteam=socketStream
        .flatMap(_.split("\\W"))
        .map((_,1))
        .updateStateByKey[Int]((seq:Seq[Int],opt:Option[Int])=>Some(seq.sum+opt.getOrElse(0)))

    resultDSteam.print()
    ssc.start()
    ssc.awaitTermination()
  }




}
