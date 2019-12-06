package com.atguigu.mysparkdstream.day07

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver




object MySource {
  def apply(host:String,port:Int)=new MySource(host,port)
}

class MySource(host:String,port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
  override def onStart(): Unit = {
      new Thread("Socket Receiver"){

        def receive(): Unit = {
            val socket = new Socket(host,port)
            val reader =new BufferedReader(new InputStreamReader(socket.getInputStream,StandardCharsets.UTF_8))
             var line:String=null
             while(!isStopped&&(line=reader.readLine())!=null){
                 store(line)
             }
          reader.close()
           socket.close()

          restart("Trying to connect again")
        }

        override def run(): Unit = {
           receive()
        }
      }.start()
  }

  override def onStop(): Unit = {}
}