package teacher.singlevalue

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-12 13:51
  */
object RDDSort1 {
    implicit val ord : Ordering[User] = new Ordering[User]{
        override def compare(x: User, y: User): Int = x.age - y.age
    }
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd = sc.parallelize(Array(User(20, "z"), User(20, "c"), User(15, "b"), User(18, "e")))
        val rdd2 = rdd.sortBy(user => user)
        println(rdd2.collect.mkString(", "))
        sc.stop()
        
    }
}

case class User(age: Int, name: String)
