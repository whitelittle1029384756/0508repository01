package com.atguigu.project2.app

import com.atguigu.project2.bean.CategoryCountInfo
import com.atguigu.project2.acc.CategoryAcc
import com.atguigu.project2.bean.UserVisitAction
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CategoryTop10App {
    def statCategoryTop10(sc:SparkContext,userVisitActionRDD: RDD[UserVisitAction]) ={
       val acc = new CategoryAcc
          sc.register(acc)
        //具体的需求
        userVisitActionRDD.foreach(visitAction=>{
             acc.add(visitAction)
        })
       // 按照品类id分组  分组后的value是一个集合 这个状态不好排序
        val actionGroupByCidMap=acc.map.groupBy(_._1._1)
        val categoryCountInfoList=actionGroupByCidMap
        .map({
            case (cid ,map)=>{
                 //封装到categorycountinfo中
                 CategoryCountInfo(
                     cid,
                     map.getOrElse((cid,"click"),0L),
                     map.getOrElse((cid,"order"),0L),
                     map.getOrElse((cid,"pay"),0L)
                 )
            }
        }).toList


       val top10 =categoryCountInfoList
         .sortBy(info=>(info.clickCount,info.orderCount,info.payCount))(Ordering.Tuple3(Ordering.Long.reverse,Ordering.Long.reverse,Ordering.Long.reverse)).take(10)

//           top10.foreach(println)
           top10
    }

}
