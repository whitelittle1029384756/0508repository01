package com.atguigu.mysparkdstream.day07

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver



object MySource3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDQueuedemo").setMaster("local[*]")
    val scc = new StreamingContext(conf,Seconds(3))

        val rddDSteam=scc.receiverStream(new MySource3("hadoop104",9999))
          .flatMap(_.split("\\W"))
          .map((_,1))
          .reduceByKey(_+_)
          .print(100)
      scc.start()
    scc.awaitTermination()

  }

}

class MySource3(host:String,port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
  override def onStart(): Unit = {
    //方法不能阻塞，所以读取数据要在一个新的线程中进行
      new Thread(){



        override def run(): Unit = {
          reciver()
        }
      }.start()

  }
    //接受数据的逻辑
  def reciver(): Unit = {
     try{

       val socket = new Socket(host,port)
       val reader=new BufferedReader(new InputStreamReader(socket.getInputStream,"utf-8"))
       var line =reader.readLine()
       while(line!=null){
         store(line)
         line=reader.readLine()

       }
       reader.close()
       socket.close()
     } catch{
       case e:Exception=>e.printStackTrace()
     }finally {
        //重启任务
        restart("重新连接")
     }

  }



  override def onStop(): Unit = {}
}