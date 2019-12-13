package wordcounttest.day03.partitioner.kvrdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author lzc
  * Date 2019-09-16 14:20
  */
object Practice {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
        val lines = sc.textFile("c:/agent.log")
        
        // RDD[(province,ads), 1))]
        val proAdsOne = lines.map(line => {
            val split: Array[String] = line.split(" ")
            ((split(1), split(4)), 1)
        })
        
        // => RDD[(province,ads), count))]   map   => RDD[(province, (ads, count))]        groupByKey
        val proAdsCount = proAdsOne.reduceByKey(_ + _).map {
            case ((pro, ads), count) => (pro, (ads, count))
        }
        // => RDD[(province, List[(ads, count)])]
        val proAdsCountGrouped = proAdsCount.groupByKey()
        
        // 排序, 取前
        val resultRDD = proAdsCountGrouped.map {
            case (pro, adsCountIt) => {
                (pro, adsCountIt.toList.sortBy(_._2)(Ordering.Int.reverse).take(3))
            }
        }.sortByKey()
    
        resultRDD.collect.foreach(println)
        
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

*/;