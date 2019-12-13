package wordcounttest.day03.partitioner.Practice

import org.apache.spark.{SparkConf, SparkContext}

object Practice {
  def main(args: Array[String]): Unit = {
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[2]")
    val sc=new SparkContext(conf)
//1516609143867 6 7 64 16
       val lines=sc.textFile("C:\\Users\\lzp\\Desktop\\agent.log")
       val proadsone=  lines.map(line=>{
           val split=  line.split(" ")
           ((split(1),split(4)),1)

         })
    //
       val proadscount=proadsone.reduceByKey(_+_).map({
         case ((provice,ads),count)=>(provice,(ads,count))
       })
       val proadscountgroupby=proadscount.groupByKey()

      val resultrdd=proadscountgroupby.map({
        case (province,adsCountIter)=>(province,adsCountIter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
      }).sortByKey()

        resultrdd.collect.foreach(println)

    sc.stop()

  }
}
