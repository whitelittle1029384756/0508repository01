package com.atguigu.mysparkdstream.day07.kafka


import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount1 {
  def main(args: Array[String]): Unit = {

     val conf = new SparkConf().setAppName("WordCount1").setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(3))

      val brokers="hadoop104:9092,hadoop105:9092,hadoop106:9092"
      val topic="first"
    //消费组名
     val group ="bigdata"

        val kafkaParams=Map(
          ConsumerConfig.GROUP_ID_CONFIG -> group,
          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers
        )

      //泛型12：key,value的类型 泛型34：keyvalue 的解码器
      val sourceDStream=KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](
         ssc,
        kafkaParams,
        Set(topic)
      )
      sourceDStream.print()
     ssc.start()
    ssc.awaitTermination()


  }

}
