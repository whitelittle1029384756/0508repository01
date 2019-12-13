package wordcounttest.day02

import org.apache.spark.{SparkConf, SparkContext}

object CreateRDD {
  def main(args: Array[String]): Unit = {
       //创建sc
    var conf= new SparkConf().setAppName("CreateRDD").setMaster("local[2]")
        var sc= new SparkContext(conf)
         sc.setLogLevel("error")
       //1.通过标准的scala集合来得到
      val list1= List(30,50,70,60,10)
        val rdd=sc.parallelize(list1)
           rdd.collect().foreach(println)

        sc.stop()

  }

}
