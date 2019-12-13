package com.atguigu.app

import com.atguigu.bean.{CategoryCountInfo, CategorySession, UserVisitAction}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object CategorySessionTopApp {
  def statCategoryTop10Session(sc: SparkContext, uservisitActionRDD: RDD[UserVisitAction], categoryTop10: List[CategoryCountInfo]): Unit = {
     //1.根据categoryTop10 把那些用户的点击记录过滤出来
       val filteredUserVisitActionRDD=uservisitActionRDD
         .filter( action => {
             categoryTop10.map(_.categoryId).contains(action.click_category_id.toString)
         })
      //RDD[(cid, sid), 1]
     //如何统计每个sid的count 先计数，后map
      val cidSidOneRDD = filteredUserVisitActionRDD.map(action =>{
       ((action.click_category_id, action.session_id), 1)
     })
    val cidSidCountItRDD =cidSidOneRDD
      .reduceByKey(_+_)
      .map(
        {
          case ((cid,sid),count) => (cid,(sid,count))
        }
      )
      .groupByKey()

    //排序，取10
    val resultRDD = cidSidCountItRDD.map({
      case (cid,sidCountIt) => {
        //sortby的前提，有自身排序的能力，或者用第三方的排序功能
        (cid,sidCountIt.toList.sortBy(-_._2).take(10))
      }
    })
    resultRDD.collect.foreach(println)

  }


  //第三种排序方法
  def statCategoryTop10Session2(sc: SparkContext, uservisitActionRDD: RDD[UserVisitAction], categoryTop10: List[CategoryCountInfo]): Unit = {
    //1.根据categoryTop10 把那些用户的点击记录过滤出来
    val filteredUserVisitActionRDD=uservisitActionRDD
      .filter( action => {
        categoryTop10.map(_.categoryId).contains(action.click_category_id.toString)
      })
    //RDD[(cid, sid), 1]
    //如何统计每个sid的count 先计数，后map
    val cidSidOneRDD = filteredUserVisitActionRDD.map(action =>{
      ((action.click_category_id, action.session_id), 1)
    })
    val cidSidCountItRDD =cidSidOneRDD
      //重新分区的目的是为了把cid相同的数据分到同一分区，方便后面的自动排序操作
      //如果不同的分区都有相同的cid，则不能达到去前10 的目的
      .reduceByKey(new MyPartitioner(categoryTop10.map(_.categoryId)),_+_)//重新分区
      .map{
      case ((cid,sid),count) => CategorySession(cid.toString,sid,count)
    }
      val resultRdd = cidSidCountItRDD.mapPartitions(it =>{
        var treeSet = new mutable.TreeSet[CategorySession]()
           it.foreach(cs =>{
             treeSet +=cs
             if(treeSet.size >10){
               treeSet = treeSet.take(10)
             }
           })
        treeSet.toIterator
      })

    resultRdd.collect().foreach(println)
  }

}


class MyPartitioner(categoryTop10: List[String])extends Partitioner() {
  private val cidIndexList = categoryTop10.zipWithIndex.toMap
  override def numPartitions: Int = categoryTop10.size

  override def getPartition(key: Any): Int = {
    key match{
          //根据对应的cid取出对应的分区编号 ，map中通过key取到分区号
      case (cid:Long) => cidIndexList(cid.toString)
    }
  }
}