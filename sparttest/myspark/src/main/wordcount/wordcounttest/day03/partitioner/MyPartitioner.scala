package wordcounttest.day03.partitioner

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object MyPartitioner {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(3, 5, 8, -3, 1, 9, 4, 6))
     val rdd3=rdd1.map((_,1)).partitionBy(new MyPartitioner(2)).map(
       (_._1)
     )
    println(rdd3.getNumPartitions)
val rdd4=rdd3.glom()
    rdd3.glom().collect.foreach(x=> println(x.mkString(",")))
      sc.stop()
  }
}

class MyPartitioner(val num:Int) extends Partitioner {
  override def numPartitions: Int = num

  override def getPartition(key: Any): Int = {
   val k= key.asInstanceOf[Int]
      (k % num).abs

  }
  override def hashCode(): Int = super.hashCode()

  override def equals(obj: scala.Any): Boolean = super.equals(obj)
}