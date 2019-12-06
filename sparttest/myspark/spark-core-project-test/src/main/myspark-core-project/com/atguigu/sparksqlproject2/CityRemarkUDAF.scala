package com.atguigu.sparksqlproject2


import java.text.DecimalFormat

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._


class CityRemarkUDAF2 extends UserDefinedAggregateFunction{
  //传入string类型
  override def inputSchema: StructType = StructType(StructField("city_name",StringType)::Nil)
//缓存类型 迭代类型   maptype要注明泛型
  override def bufferSchema: StructType = StructType(StructField("city_count",MapType(StringType,LongType))::StructField("total",LongType)::Nil)
//最终类型
  override def dataType: DataType = StringType

  override def deterministic: Boolean = true
  //初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0)=Map[String,Long]()
      buffer(1)=0L
  }
  //分区类迭代
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if(!input.isNullAt(0)) {

      val cityName=input.getString(0)
       var citycountmap=buffer.getMap[String,Long](0)
       citycountmap+=cityName->(citycountmap.getOrElse(cityName,0L)+1L)

      buffer(0)=citycountmap
      buffer(1)=buffer.getLong(1)+1L  //统计所有点击的总数
    }

  }
  //分区间的聚合
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    val   map1=buffer1.getMap[String,Long](0)
    val   map2=buffer2.getMap[String,Long](0)
     //map间如何聚合？不能用集合间的并集
       val resultmap=map2.foldLeft(map1){
         case (map,(cityname,count))=>
            map+(cityname->(map.getOrElse(cityname,0L)+count))

       }
    //返回buffer1的结果
    buffer1(0)=resultmap
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)

  }
  //最终返回值
  override def evaluate(buffer: Row): Any = {
      val map=buffer.getMap[String,Long](0)
      val total=buffer.getLong(1)
      //排序 取前2
        var Top2List=map.toList.sortBy(-_._2).take(2)
          .map({case(city,count)=>{CityRemark(city,count.toDouble/total)}})
         Top2List:+=CityRemark("其它",Top2List.foldLeft(1D)((rate,cr)=>rate-cr.rate))

       Top2List.mkString(",")
  }
}


case class CityRemark(cityname:String,rate:Double){

   val formater=new DecimalFormat(".00%")

  override def toString: String = s"$cityname:${formater.format(rate)}"

}