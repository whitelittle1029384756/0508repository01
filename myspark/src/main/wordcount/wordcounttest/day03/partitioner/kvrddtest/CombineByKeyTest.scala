package wordcounttest.day03.partitioner.kvrddtest

import org.apache.spark.{SparkConf, SparkContext}

object CombineByKeyTest {
  def main(args: Array[String]): Unit = {
    val conf= new SparkConf().setAppName("WordCount").setMaster("local[2]")
    val sc=new SparkContext(conf)

//    val rdd1 = sc.parallelize(Array("hello", "hello", "world", "hello", "atguigu", "hello", "atguigu", "atguigu"))
//    //注意要kv的集合和数组才能使用，value的无法使用
//     val rdd2=rdd1.map((_,1))
//      val rdd3=rdd2.combineByKey(v=>v,(last:Int,v:Int)=>(last+v),(v1:Int,v2:Int)=>v1+v2)
//       rdd3.collect.foreach(println)
  //求平均数
    val rdd1 = sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)),2)
   //此处第二，第三个函数的形参的参数类型不可省略
    val rdd3=rdd1.combineByKey(v=>(v,1),
     ((sum1:(Int,Int),t:Int)=>(sum1._1+t,sum1._2+1)),
     ((sum2:(Int,Int),sum3:(Int,Int))=>(sum2._1+sum3._1,sum2._2+sum3._2))).map(
      {case (k,(sum,count))=>(k,(sum.toDouble/count))}
   )

     rdd3.collect.foreach(println)
    sc.stop()

  }
}
