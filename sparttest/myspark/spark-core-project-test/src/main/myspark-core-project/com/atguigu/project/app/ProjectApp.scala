package com.atguigu.project.app

import com.atguigu.project.bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ProjectApp {
  def main(args: Array[String]): Unit = {
    // 1. sc初始化好
    val conf: SparkConf = new SparkConf().setAppName("ProjectApp").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)
     //2.读取数据
     val lineRdd= sc.textFile("C:\\Users\\lzp\\Desktop\\user_visit_action(1).txt")

    //3.封装到样例类中
    val userVisitActionRDD: RDD[UserVisitAction] = lineRdd.map(line => {
      val splits: Array[String] = line.split("_")
      UserVisitAction(
        splits(0),
        splits(1).toLong,
        splits(2),
        splits(3).toLong,
        splits(4),
        splits(5),
        splits(6).toLong,
        splits(7).toLong,
        splits(8),
        splits(9),
        splits(10),
        splits(11),
        splits(12).toLong)
    })

    // 需求1: top10的热门品类
/*   val  top10CategoryCountInfo=CategoryTop10App.statCategoryTop10(sc, userVisitActionRDD)
   //需求2：
//      CategorySessionTopApp.statCategoryTop10Session_1(sc,userVisitActionRDD,top10CategoryCountInfo)
      println("-------------------------------------------")
    CategorySessionTopApp.statCategoryTop10Session(sc,userVisitActionRDD,top10CategoryCountInfo)
    println("====================================================")
    CategorySessionTopApp.statCategoryTop10Session_2(sc,userVisitActionRDD,top10CategoryCountInfo)*/
    PageConversionApp.calcPageConversion(sc,userVisitActionRDD, "1,2,3,4,5,6,7")
    sc.stop()
  }
}
