package wordcounttest.day03.partitioner.agrratebykey

import org.apache.spark.{SparkConf, SparkContext}

object AgrrateByKey {
  def main(args: Array[String]): Unit = {
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[2]")
    val sc=new SparkContext(conf)

      val rdd1=sc.parallelize(List(("a",3), ("a",2), ("c",4), ("b",3), ("c",6), ("c",8)),2)
//          val rdd3=rdd1.aggregateByKey(Int.MinValue)(_.max(_),_+_)

/*    val rdd3 = rdd1.aggregateByKey((Int.MinValue, Int.MaxValue))(
      (maxMin, v) => (maxMin._1.max(v), maxMin._2.min(v)) ,
      (maxMin1, maxMin2) => (maxMin1._1 + maxMin2._1, maxMin1._2 + maxMin2._2)
    )*/
      /*  val rdd3=rdd1.aggregateByKey((Int.MinValue,Int.MaxValue))(
          //此处注意max和min的顺序，搞清楚关系
          ( maxmin,x)=>(maxmin._1.max(x),maxmin._2.min(x)),((maxmin1,maxmin2)=>(maxmin1._1 + maxmin2._1,maxmin1._2 + maxmin2._2)))*/
//        val rdd3=rdd1.aggregateByKey((Int.MinValue,Int.MaxValue))(
//        ({case (x,y)=>(x._1.max(y),x._2.min(y)) })
//        ,({
//          case (x,y)=>(x._1+y._1,x._2+y._2)
//        }
//        ))

    //平均数计算 (b,(sum,count))
    val rdd3=rdd1.aggregateByKey((0,0))(
      ((kv,x)=>(kv._1+x,kv._2+1)),((kv1,kv2)=>(kv1._1+kv2._1,kv1._2+kv2._2))
    ).map({case (x,(sum,count))=>(x,(sum.toDouble/count))})

          rdd3.collect.foreach(println)
    sc.stop()
  }


}
