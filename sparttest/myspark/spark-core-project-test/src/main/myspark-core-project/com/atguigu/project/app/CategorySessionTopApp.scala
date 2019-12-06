package com.atguigu.project.app

import com.atguigu.project.bean.{CategoryCountInfo, CategorySession, UserVisitAction}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object CategorySessionTopApp {
   def statCategoryTop10Session(sc: SparkContext, userVisitActionRDD: RDD[UserVisitAction], top10CategoryCountInfo: List[CategoryCountInfo]) = {
      //1.包含top10cid的那些用户的点击记录过滤出来
      val filteredUserVisitActionRDD = userVisitActionRDD.filter(action => {
         top10CategoryCountInfo.map(_.categoryId).contains(action.click_category_id.toString)
      })
      //RDD[(cid,sid),1]
      val cidSidOneRDD = filteredUserVisitActionRDD.map(action => ((action.click_category_id, action.session_id), 1))
      //RDD[(cid,sid),count]  RDD[cid,(sid,count)] RDD[(cid,Interaot[sid,count])]
      val cidSidCountItRDD = cidSidOneRDD.reduceByKey(_ + _)
        .map({
           case ((cid, sid), count) => (cid, (sid, count))
        }).groupByKey()

      //排序，取前10
      val resultRDD = cidSidCountItRDD.map({
         //以二元元组的形式在迭代器中保存
         case (cid, sidCountIt) => (cid, sidCountIt.toList.sortBy(-_._2).take(10))

      })
      resultRDD.collect.foreach(println)

   }

   //思路二：

   def statCategoryTop10Session_1(sc: SparkContext, userVisitActionRDD: RDD[UserVisitAction], top10CategoryCountInfo: List[CategoryCountInfo]) = {
      //1.包含top10cid的那些用户的点击记录过滤出来
      val filteredUserVisitActionRDD = userVisitActionRDD.filter(action => {
         top10CategoryCountInfo.map(_.categoryId).contains(action.click_category_id.toString)
      })
      //RDD[(cid,sid),1]
      val cidSidOneRDD = filteredUserVisitActionRDD.map(action => ((action.click_category_id, action.session_id), 1))
      //RDD[(cid,sid),count]  RDD[cid,(sid,count)] RDD[(cid,Interaot[sid,count])]
      val cidSidCountItRDD = cidSidOneRDD.reduceByKey(_ + _)
        .map({
           case ((cid, sid), count) => (cid, (sid, count))
        }).groupByKey()

      //排序，取前10
       top10CategoryCountInfo.map(_.categoryId).foreach(cid=>{
         //此处_._1是一个元组，cid也是一个元组
          val onlyCidRDD=cidSidCountItRDD.filter(_._1.toString==cid)
           val sidCountRDD=onlyCidRDD.flatMap({
              case (_,it)=>it
           })
          val result=sidCountRDD
            .sortBy(_._2,ascending = false).take(10)
            .map({case (sid,count)=>CategorySession(cid,sid,count)})
              result.foreach(println)
            println("---------------------------------")
       })
      //写入外部存储：jdbc,hive,hbase...
//      resultRDD.collect.foreach(println)

   }

//思路三：
   def statCategoryTop10Session_2(sc: SparkContext, userVisitActionRDD: RDD[UserVisitAction], top10CategoryCountInfo: List[CategoryCountInfo]) = {
      //1.包含top10cid的那些用户的点击记录过滤出来
      val filteredUserVisitActionRDD = userVisitActionRDD.filter(action => {
         top10CategoryCountInfo.map(_.categoryId).contains(action.click_category_id.toString)
      })
      //RDD[(cid,sid),1]
      val cidSidOneRDD = filteredUserVisitActionRDD.map(action => ((action.click_category_id, action.session_id), 1))
      //RDD[(cid,sid),count]  RDD[cid,(sid,count)] RDD[(cid,Interaot[sid,count])]
      val cidSidCountItRDD = cidSidOneRDD
        .reduceByKey(new MyPartitioner(top10CategoryCountInfo.map(_.categoryId)),_+_)
        .map({
          case ((cid,sid),count)=>CategorySession(cid.toString,sid,count)

        })
       val resultRdd=cidSidCountItRDD.mapPartitions(it=>{
           var treeSet=new mutable.TreeSet[CategorySession]()
            it.foreach(cs =>{
               treeSet+=cs
               if(treeSet.size>10){
                  treeSet=treeSet.take(10)
               }
            })
          treeSet.toIterator
       })

        resultRdd.collect.foreach(println)

      //写入外部存储：jdbc,hive,hbase...
      //      resultRDD.collect.foreach(println)

   }
}

//根据cid的个数进行分区

class MyPartitioner(categoryIdTop10:List[String])extends Partitioner {

  private val cidIndexList = categoryIdTop10.zipWithIndex.toMap

  override def numPartitions: Int = categoryIdTop10.size

  override def getPartition(key: Any): Int = {
    key match {
      case (cid: Long, _) => cidIndexList(cid.toString)

    }
  }
}