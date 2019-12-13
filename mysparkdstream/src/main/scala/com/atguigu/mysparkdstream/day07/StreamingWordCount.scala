package com.atguigu.mysparkdstream.day07

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWordCount {
  def main(args: Array[String]): Unit = {
      val conf= new SparkConf().setAppName("StreamingWordCount")
        .setMaster("local[2]")
    //创建sparkstreaming的入口对象
    val ssc = new StreamingContext(conf,Seconds(3))
 //创建一个DStream
    val lines=ssc.socketTextStream("hadoop104",9999)
  //3.单词的处理
    val words=lines.flatMap(_.split("""\s+"""))
    //4.单词形成元组
    val wordAndOne=words.map((_,1))
    //5.统计单词的个数
      val count =wordAndOne.reduceByKey(_+_)
    //6.显示
    println("aaa")
    count.print
    //7.开始接受数据并计算
    ssc.start()
    //8.等待计算结束
    ssc.awaitTermination()

  }
}
