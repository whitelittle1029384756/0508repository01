package com.atguigu.project2.app

import com.atguigu.project2.bean.CategorySession
import com.atguigu.project2.bean.{CategoryCountInfo, UserVisitAction}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CategorySessionTopApp {
    def statCategoryTop10Session(sc:SparkContext,uservisitActionRDD:RDD[UserVisitAction],top10CategoryCountInfo:List[CategoryCountInfo]): Unit ={
        //1.包含top10 cid 的用户过滤出来
        val filterUserVisitActionRDD=uservisitActionRDD.filter(action=>{
               top10CategoryCountInfo.map(_.cateforyId).contains(action.click_category_id.toString)
        })
      //2.转换成可以计算的rdd RDD[(cid,sid),1]
        val cidSidOneRDD= filterUserVisitActionRDD.map(action=>{
          ((action.click_category_id,action.session_id),1)
        })
       val cidSidOneRDD2=filterUserVisitActionRDD.map({
         case o:UserVisitAction=>((o.click_category_id,o.session_id),1)
       })
      //3.转换成RDD[(cid,sid),count] RDD[cid,(sid,count)] RDD[(cid,Iteraot[sid,count])]
      val cidSidCountItRDD=   cidSidOneRDD
        .reduceByKey(_+_)
        .map({
          case ((cid,sid),count)=>(cid,(sid,count))
        })
        .groupByKey()
       //排序，取前10  map无法排序，要转换后排序  记住rdd的形态，cid不变，后面集合取前10 改变._2的长度
      val resultRDD=cidSidCountItRDD
        .map({
          case (cid,sidCountIt)=>(cid,sidCountIt.toList.sortBy(-_._2).take(10))})

        resultRDD.collect.foreach(println)
    }
    //思路二：不用list,开启多个job

  def statCategoryTop10Session_1(sc:SparkContext,uservisitActionRDD:RDD[UserVisitAction],top10CategoryCountInfo:List[CategoryCountInfo]): Unit ={
    //1.包含top10 cid 的用户过滤出来
    val filterUserVisitActionRDD=uservisitActionRDD.filter(action=>{
      top10CategoryCountInfo.map(_.cateforyId).contains(action.click_category_id.toString)
    })
    //2.转换成可以计算的rdd RDD[(cid,sid),1]
    val cidSidOneRDD= filterUserVisitActionRDD.map(action=>{
      ((action.click_category_id,action.session_id),1)
    })
    val cidSidOneRDD2=filterUserVisitActionRDD.map({
      case o:UserVisitAction=>((o.click_category_id,o.session_id),1)
    })
    //3.转换成RDD[(cid,sid),count] RDD[cid,(sid,count)] RDD[(cid,Iteraot[sid,count])]
    val cidSidCountItRDD=   cidSidOneRDD
      .reduceByKey(_+_)
      .map({
        case ((cid,sid),count)=>(cid,(sid,count))
      })
      .groupByKey()
    //排序，取前10  map无法排序，要转换后排序  先排（sid,count）最后加上cid,要封装
          top10CategoryCountInfo.map(_.cateforyId).foreach(cid=>{
             val onlyCidRDD=cidSidCountItRDD.filter(_._1.toString==cid)
             val sidCountRDD=onlyCidRDD.flatMap({
               case (_,it)=> it
             })
            val result=sidCountRDD
              .sortBy(_._2,ascending = false)
              .take(10)
              .map({
                case (sid,count)=>CategorySession(cid,sid,count)
              })
             result.foreach(println)
          })


  }





}
