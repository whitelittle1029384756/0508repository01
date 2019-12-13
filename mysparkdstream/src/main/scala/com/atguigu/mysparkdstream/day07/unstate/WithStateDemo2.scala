package com.atguigu.mysparkdstream.day07.unstate

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WithStateDemo2 {


  def createSsc()= {
    println("aaaa")
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("HighKafka")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    ssc.checkpoint("hdfs://hadoop104:9000/spark/ck1_kafkatest")
    //kafka 参数
    //kafka 参数声明
    val brokers="hadoop104:9092,hadoop105:9092,hadoop106:9092"
    val topic ="first"
    val group ="bigdata"

    val kafkaParams = Map(
      ConsumerConfig.GROUP_ID_CONFIG->group,
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->brokers
    )
    //
    val sourceDStream=KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](
      ssc,
      kafkaParams,
      Set(topic)
    )
    val result=
      sourceDStream.flatMap((_._2.split("\\W")))
        .map((_,1))
          .updateStateByKey[Int]((seq:Seq[Int],opt:Option[Int])=>Some(seq.sum+opt.getOrElse(0)))



     result.print()
    ssc
  }

  def main(args: Array[String]): Unit = {
      val ssc=StreamingContext.getActiveOrCreate("hdfs://hadoop104:9000/spark/ck1_kafkatest",createSsc)

     ssc.start()
    ssc.awaitTermination()

    }




}
