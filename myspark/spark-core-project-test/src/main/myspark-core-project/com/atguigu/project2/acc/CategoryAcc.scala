package com.atguigu.project2.acc

import com.atguigu.project2.bean.UserVisitAction
import org.apache.spark.util.AccumulatorV2

class CategoryAcc extends AccumulatorV2[UserVisitAction,Map[(String,String),Long]]{
    var map =Map[(String,String),Long]()
  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[UserVisitAction, Map[(String, String), Long]] = {
        val acc= new CategoryAcc
         acc.map++=map
       acc
  }


  override def reset(): Unit = {
      map=Map[(String,String),Long]()
  }
  //把次数累加到累加器
  override def add(v: UserVisitAction): Unit = {
         if(v.click_category_id != -1){
           map += (v.click_category_id.toString,"click")->(map.getOrElse((v.click_category_id.toString,"click"),0L)+1L)
         }else if(v.order_category_ids != "null"){
              val orderIds=v.order_category_ids.split(",")
               orderIds.foreach(id=>{
                    map+=(id,"order")->(map.getOrElse((id,"order"),0L)+1L)
               })
         }else if(v.pay_category_ids !="null"){
             val payIds=v.pay_category_ids.split(",")
               payIds.foreach(id=>{
                  map+= (id,"pay")->(map.getOrElse((id,"pay"),0l)+1L)
               })
         }
  }

  override def merge(other: AccumulatorV2[UserVisitAction, Map[(String, String), Long]]): Unit = {
       val o = other.asInstanceOf[CategoryAcc]
        //map直接相加为两个集合相加，元素的k相同覆盖，遍历后将value相加
         o.map.foreach(
            {
              case (cidAction,count)=>{
                this.map+=cidAction ->(this.map.getOrElse(cidAction,0L)+count)
              }
            })
  }

  override def value: Map[(String, String), Long] = map
}
