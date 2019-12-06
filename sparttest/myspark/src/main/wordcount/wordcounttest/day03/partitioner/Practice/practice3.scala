package wordcounttest.day03.partitioner.Practice

import org.apache.spark.{SparkConf, SparkContext}

object practice3 {
  def main(args: Array[String]): Unit = {
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[2]")
    val sc=new SparkContext(conf)
    val lines=sc.textFile("C:\\Users\\lzp\\Desktop\\agent.log")
    val proadsone=lines.map(x=>{
      val split=x.split(" ")
      ((split(1),split(4)),1)
    })
     val proadscount=proadsone.reduceByKey(_+_)
      val progroupby=proadscount.map({
        case ((pro,ads),count)=>(pro,(ads,count))
      }).groupByKey()
      val result=progroupby.map({
        case (pro,(adscountInter)) =>(pro,adscountInter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
      }).sortByKey()

    result.collect.foreach(println)
      sc.stop()
  }
}
/*
1516609143867 6 7 64 16
1516609143869 9 4 75 18
1516609143869 1 7 87 12

统计出每一个省份广告被点击次数的 TOP3
统计出每一个省份每个城市广告被点击次数的 TOP3

倒推法:
=> ...
=> RDD[(province,ads), 1))]    reduceByKey
=> RDD[(province,ads), count))]   map
=> RDD[(province, (ads, count))]        groupByKey
=> RDD[(province, List[(ads, count)])]

*/