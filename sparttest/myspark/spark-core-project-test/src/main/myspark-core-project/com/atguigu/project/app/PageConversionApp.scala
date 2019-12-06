package com.atguigu.project.app

import java.text.DecimalFormat

import com.atguigu.project.bean.UserVisitAction
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object PageConversionApp {
  def calcPageConversion(sc:SparkContext, userVisitActionRDD:RDD[UserVisitAction],pages:String ): Unit ={

       //1.得到目标跳转流
        val splits=   pages.split(",")
        val prepages=splits.slice(0,splits.length-1)
        val postpages=splits.slice(1,splits.length)
      //得到工作流
      val pageFlows=prepages.zip(postpages).map({
        case (pre,post)=>(pre+"->"+post)
      })
//         pageFlows.foreach(println)

      //得到目标页面的点击量
     val targePageCount=userVisitActionRDD.filter({
         action=>{ pageFlows.contains(action.page_id.toString)}
     }).map(action=>(action.page_id,1))
       .countByKey()

     //计算页面跳转流的数量

    val totalPageFlows=userVisitActionRDD
      .groupBy(_.session_id)
      .flatMap(
         {
           case (_,iter)=>{
              val list= iter.toList.sortBy(_.action_time)
               val preactions= list.slice(0,list.length-1)
                val postactions =list.slice(1,list.length)
                val totalPageFlows=preactions.zip(postactions).map({
                  case (preaction,postaction)=>preaction.page_id+ "->"+postaction.page_id
                  //  totalPageFlows.filter(flow => pageFlow.contains(flow)).map((_, 1))
                })
             totalPageFlows.filter(flow=>pageFlows.contains(flow)).map((_,1))
           }
         }
      ).countByKey()


    //计算跳转率
    /*        val result = totalPageFlows.map {
            // 1->2
            case (flow, flowCount) =>
                val page = flow.split("->")(0)
                val rate = flowCount.toDouble / targetPageCount.getOrElse(page.toLong, Long.MaxValue)
                val formater = new DecimalFormat(".00%")

                (flow, formater.format(rate))
        }
        println(result)*/

     val result =totalPageFlows.map({
       case (flow,flowcount)=>{
            val page=flow.split("->")(0)
            val rate=flowcount.toDouble/targePageCount.getOrElse(page.toLong,Long.MaxValue)
//           val formater= new DecimalFormat(".00%")
//         (flow,formater.format(rate))
         println(rate)
       }
     })
      println(result)

  }
}
