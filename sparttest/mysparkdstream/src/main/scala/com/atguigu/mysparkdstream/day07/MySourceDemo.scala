package com.atguigu.mysparkdstream.day07

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MySourceDemo {
  def main(args: Array[String]): Unit = {
      val conf = new SparkConf().setAppName("MySourceDemo").setMaster("local[*]")
      val scc = new StreamingContext(conf,Seconds(5))

      val lines= scc.receiverStream[String](MySource("hadoop104",9999))

      val words=lines.flatMap(_.split("""\s+"""))
    val wordAndOne=words.map((_,1))
      val count=wordAndOne.reduceByKey(_+_)

        count.print()
    scc.start()
    scc.awaitTermination()
     scc.stop(false)



  }
}
