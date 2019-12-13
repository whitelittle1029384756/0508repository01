package com.atguigu.project2.app


import com.atguigu.project2.app.CategoryTop10App
import com.atguigu.project2.bean.UserVisitAction
import org.apache.spark.{SparkConf, SparkContext}

object ProjectApp1 {
  def main(args: Array[String]): Unit = {
      //创建sc
           val conf = new SparkConf().setAppName("ProjectApp").setMaster("local[2]")
              val sc = new SparkContext(conf)
      //2.读取数据
       val lineRDD= sc.textFile("C:\\\\Users\\\\lzp\\\\Desktop\\\\user_visit_action(1).txt")
     //3.封装到样例类中
      val userVisitActionRDD=   lineRDD.map(line=>{
             val splits= line.split("_")
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
    //1.需求一：top10的热门品类
     val top10CategoryCountInfo=  CategoryTop10App.statCategoryTop10(sc,userVisitActionRDD)
   //2.需求二： top10品类中, 每个品类的top10活跃session
    CategorySessionTopApp.statCategoryTop10Session_1(sc,userVisitActionRDD,top10CategoryCountInfo)
//   Array("a").slice

  }
}
