package com.atguigu.project.app

import com.atguigu.project.acc.CategoryAcc
import com.atguigu.project.bean.{CategoryCountInfo, UserVisitAction}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CategoryTop10App {
  def statCategoryTop10(sc:SparkContext, userVisitActionRDD: RDD[UserVisitAction]) ={
    val acc: CategoryAcc = new CategoryAcc
          sc.register(acc)
      //具体的需求
      userVisitActionRDD.foreach(visitAction=>{
          acc.add(visitAction)
      })
      //按照品类id分组
    /*    val actionGoupedByCidMap: Map[String, Map[(String, String), Long]] = acc.map.groupBy(_._1._1)
        val categoryCountInfoList: List[CategoryCountInfo] = actionGoupedByCidMap
            .map {
                case (cid, map) =>
                    CategoryCountInfo(
                        cid,
                        map.getOrElse((cid, "click"), 0L),
                        map.getOrElse((cid, "order"), 0L),
                        map.getOrElse((cid, "pay"), 0L))
            }
            .toList*/
      val actionGroupByCidMap=acc.map.groupBy(_._1._1)

      val categoryCountInfoList=actionGroupByCidMap.map(
        {case (cid,map)=>CategoryCountInfo(
            cid,
          map.getOrElse((cid,"click"),0L),
          map.getOrElse((cid,"order"),0L),
          map.getOrElse((cid,"pay"),0L)
        )}
      ).toList

    /*  val top10 = categoryCountInfoList
            .sortBy(info => (info.clickCount, info.orderCount, info.payCount))(Ordering.Tuple3(Ordering.Long.reverse, Ordering.Long.reverse, Ordering.Long.reverse))
            .take(10)
        top10*/
      val top10=categoryCountInfoList.sortBy(info=>(info.clickCount,info.orderCount,info.payCount))(Ordering.Tuple3(Ordering.Long.reverse,Ordering.Long.reverse,Ordering.Long.reverse)).take(10)
//               top10.foreach(println)
     top10
  }
}
