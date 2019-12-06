package com.atguigu.mysparkdstream.day07.kafka

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount3 {
     val brokers="hadoop104:9092,hadoop105:9092,hadoop106:9092"
  val topic ="first"
    val group ="bigdata"
  val kafkaParams=Map(
         ConsumerConfig.GROUP_ID_CONFIG -> group,
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ->brokers
  )
    val kafkaCluster= new KafkaCluster(kafkaParams)
  //读取offsets
  def readOffsets()={
     var resultMap=Map[TopicAndPartition,Long]()
      val topicAndPartitionEither=kafkaCluster.getPartitions(Set(topic))
      topicAndPartitionEither match {
        case Right(topicAndPartionSet)=>
          //分区存在，就获取分区和偏移量
        val topicAndPatitionOffsetEither =
          kafkaCluster.getConsumerOffsets(group,topicAndPartionSet)

          if(topicAndPatitionOffsetEither.isRight){//表示曾经消费过，有offset
            val topicAndPartitiionOffsetMap=topicAndPatitionOffsetEither.right.get
           resultMap++=topicAndPartitiionOffsetMap
           }else{//表示第一次消费分区，把每个分区的偏移量置为0
            topicAndPartionSet.foreach(topicAndPartition=>{
               resultMap += topicAndPartition->0L
            })
          }
        case _ =>
      }
    resultMap

  }

  //提交offsets
  def writeOffsets(sourceDStream:InputDStream[String])={
     sourceDStream.foreachRDD(rdd=>{
        var map= Map[TopicAndPartition,Long]()
        //强转成HasOffsetRanges,包含了本次消费的offset起始范围
       val hasOffsetRanges=rdd.asInstanceOf[HasOffsetRanges]
       val rangs=hasOffsetRanges.offsetRanges
        rangs.foreach(rang=>{
            val offset=rang.untilOffset
           map+=rang.topicAndPartition()->offset
        })
           kafkaCluster.setConsumerOffsets(group,map)
     })

  }





  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("HighKafka")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

       val offsets=readOffsets()
      val sourceDStream=KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String](
        ssc,
        kafkaParams,
        readOffsets(),
        (mm:MessageAndMetadata[String,String])=>mm.message()

      )
      sourceDStream.flatMap(_.split("\\W")).map((_,1)).reduceByKey(_+_).print(100)
         writeOffsets(sourceDStream)
    ssc.start()
    ssc.awaitTermination()
  }
}
