package com.atguigu.app

import com.atguigu.bean.UserVisitAction
import org.apache.spark.{SparkConf, SparkContext}


//整体入口
object Practiceapp {
  def main(args: Array[String]): Unit = {
    //1.初始化sc
    val conf = new SparkConf().setAppName("Practiceapp").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //2.读取数据
    val lineRDD = sc.textFile("C:\\Users\\lzp\\Desktop\\user_visit_action(1).txt")

    //3.封装到样例类中 此处的数据格式一致，直接封装就好
    //封装便于序列化，更好存储
    val uservisitActionRDD = lineRDD
      .map(line => {
        val splits = line.split("_")
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
      //需求1
    //为何用object，不用class 。此处是对象名。方法名，用object
      val categoryTop10 = CategoryTop10App.statCategoryTop10(sc,uservisitActionRDD)
    println(categoryTop10)
     //需求二
    CategorySessionTopApp.statCategoryTop10Session(sc,uservisitActionRDD,categoryTop10)
  }



}
