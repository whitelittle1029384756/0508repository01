package wordcounttest.day05.add

import org.apache.spark.{SparkConf, SparkContext}

object Add1 {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)

     val rdd1=sc.parallelize(Array(1,2,3,4))
      val acc =new MyAccTest
    //要向sc注册自定义的累加器
    sc.register(acc)
      val rdd3=rdd1.map(x=>{
           acc.add(1)
        (x,1)
      })
    rdd3.collect
       println("----------------")
      println(acc.value)
    sc.stop()
  }
}
