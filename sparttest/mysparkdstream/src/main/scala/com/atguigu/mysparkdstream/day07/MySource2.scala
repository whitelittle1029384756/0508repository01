package com.atguigu.mysparkdstream.day07

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

class MySource2(val host:String ,val port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
  override def onStart(): Unit = {
       new Thread(){



         override def run(): Unit = {
            receiver()
         }
       }
  }
  def receiver(): Unit = {

  }
  override def onStop(): Unit = {}
}

