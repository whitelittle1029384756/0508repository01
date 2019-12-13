package wordcounttest.day05.add

import org.apache.spark.{SparkConf, SparkContext}

object Add2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("Add1").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
    val acc = new MyAccTest2
    sc.register(acc)
    val rdd2 = sc.parallelize(Array(3, 5, 8, 2, 1, 9, 5, 7))
    // sum, avg, max, min
    rdd2.foreach(x => acc.add(x))

    println(acc.value)
    sc.stop()


  }
}
