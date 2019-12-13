package wordcounttest.day03.partitioner.Practice

import org.apache.spark.{SparkConf, SparkContext}

object Practice2 {
  def main(args: Array[String]): Unit = {
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[2]")
    val sc=new SparkContext(conf)
//    时间戳，省份，城市，用户，广告，字段使用空格分割
    //1516609143867 6 7 64 16
     //求每个省份每个城市的top3
    val lines=sc.textFile("C:\\Users\\lzp\\Desktop\\agent.log")
    val proadsone=  lines.map(line=>{
//      val split=  line.split(" ")
//      (((split(1),split(2)),split(4)),1)
//
//    })

      val split=  line.split(" ")
      ((split(1),split(4)),1)

    })

    //    RDD[ (province,city),List[(ads, count)]]
//    val proadscount=proadsone.reduceByKey(_+_).map({
//      case (((provice,city),ads),count)=>((provice,city),(ads,count))
//    })
//    val proadscountgroupby=proadscount.groupByKey()
//
//    val resultrdd=proadscountgroupby.map({
//      case (procity,adsCountIter)=>(procity,adsCountIter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
//    }).sortByKey()
//
//    resultrdd.collect.foreach(println)
       val proadscount=proadsone.reduceByKey(_+_).map(
  {
    case (pro,count)=>(pro._1,(pro._2,count))
  })
    val proadscountgroupby=proadscount.groupByKey()
     val  resultrdd=proadscountgroupby.map({
       case (pro,k)=>(pro,k.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
     }).sortByKey()

        resultrdd.collect.foreach(println)
    sc.stop()
  }
}
