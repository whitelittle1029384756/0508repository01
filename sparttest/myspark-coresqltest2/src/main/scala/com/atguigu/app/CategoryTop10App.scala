package com.atguigu.app

import com.atguigu.acc.MapAccumulator
import com.atguigu.bean.{CategoryCountInfo, UserVisitAction}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CategoryTop10App {
  def statCategoryTop10(sc: SparkContext, uservisitActionRDD: RDD[UserVisitAction]) = {
      //如何处理 用累加器来处理数据
    val acc = new MapAccumulator
    //注册累加器
    sc.register(acc)
    //具体需求  需要遍历数据 为何不用foreachpartition

    uservisitActionRDD.foreach(action => {
      //将每一个元素放入累加器中

      acc.add(action)
    })
    //将累加器中的数据排序并取出来
    //按照商品id分组 相同的value成一个集合
    // ("1","click") -> 1000   ("1", "order") -> 200
    val actionGroupByCidMap = acc.map.groupBy(_._1._1)
       //遍历集合并把value中的元素封装，计数
     val categoryCountInfoList=actionGroupByCidMap
       .map( {case (cid,map) => {
         CategoryCountInfo(
           cid,
           map.getOrElse((cid,"click"),0L),
           map.getOrElse((cid,"order"),0L),
           map.getOrElse((cid,"pay"),0L)
         )

       }}).toList

    //取出top10并排序
 /*    val top111= categoryCountInfoList
    println(top111)*/
     val top10 = categoryCountInfoList
       //三个元素如何排序
       .sortBy(info => (info.clickCount,info.orderCount,info.payCount))(Ordering.Tuple3(Ordering.Long.reverse,Ordering.Long.reverse,Ordering.Long.reverse))
       .take(10)
    top10
  }



}
