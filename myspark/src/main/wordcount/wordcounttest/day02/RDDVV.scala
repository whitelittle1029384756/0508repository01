package wordcounttest.day02

import org.apache.spark.{SparkConf, SparkContext}

object RDDVV {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(30, 50, 70, 60, 10, 20))
    val rdd2 = sc.parallelize(Array(30, 40, 70, 5, 12, 20))

//      val rdd3=rdd1++rdd2
//      val rdd3=rdd1.subtract(rdd2)
//        val rdd3=rdd1.intersection(rdd2)
//        val rdd3=rdd1.zip(rdd2)
//         val rdd3=rdd1.zipWithIndex()
        val rdd3=rdd1.zipPartitions(rdd2)((it1,it2)=>it1.zip(it2))


    println(rdd3.getNumPartitions)
        rdd3.glom().collect().foreach(arr=>println(arr.mkString(", ")))


  }
}
