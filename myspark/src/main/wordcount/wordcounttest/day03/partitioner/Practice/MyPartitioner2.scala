package wordcounttest.day03.partitioner.Practice

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 09:22
  */
object MyPartitioner {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(Array(3, 5, 8, -3, 1, 9, 4, 6))
        
        val rdd2 = rdd1.map((_, null))
        // partitionBy 只能用在键值对形式的RDD上, 所以需要先把需要的RDD转换成RDD[k-v]
        var rdd3 = rdd2.partitionBy(new MyPartitioner(2)).map(_._1)
        rdd3.glom().collect().foreach(x => println(x.mkString(", ")))
        
        sc.stop()
    }
}

class MyPartitioner(val num: Int) extends Partitioner {
    override def numPartitions: Int = num
    
    override def getPartition(key: Any): Int = {
        val k = key.asInstanceOf[Int]
        (k % 2).abs
//        0
    }
    
    override def hashCode(): Int = super.hashCode()
    
    override def equals(obj: scala.Any): Boolean = super.equals(obj)
}
