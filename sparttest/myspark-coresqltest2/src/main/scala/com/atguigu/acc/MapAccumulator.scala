package com.atguigu.acc

import com.atguigu.bean.UserVisitAction

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

class MapAccumulator extends AccumulatorV2[UserVisitAction,Map[(String,String),Long]]{
 //定义一个map存放变量
  //map[(cid,action),count]
  var map = Map[(String,String),Long]()
  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[UserVisitAction, Map[(String, String), Long]] = {
      val acc = new MapAccumulator
       acc.map ++= map
      acc
  }

  override def reset(): Unit = {
      map = Map[(String,String),Long]()
  }
   //分区间聚合
  override def add(v: UserVisitAction): Unit = {
      if(v.click_category_id != -1){
        map += (v.click_category_id.toString,"click") -> (map.getOrElse((v.click_category_id.toString,"click"),0L )+1L)
      }else if(v.click_category_id !="null"){
         val orderIds = v.order_category_ids.split(",")
        orderIds.foreach(id =>{
           map += (id,"order") -> (map.getOrElse((id,"order"),0L)+ 1L)
        }
        )}else if(v.pay_category_ids !="null"){
           val payIds= v.pay_category_ids.split(",")
            payIds.foreach( id => {
               map += (id,"pay") -> (map.getOrElse((id ,"pay"),0L)+ 1L)
            })
        }

  }

  override def merge(other: AccumulatorV2[UserVisitAction, Map[(String, String), Long]]): Unit = {

    val o =other.asInstanceOf[MapAccumulator]
    o.map.foreach
    {case (cidAction,count) => {
       this.map += cidAction ->(this.map.getOrElse(cidAction,0L)+count)   } }
  }

  override def value: Map[(String, String), Long] = map
}
