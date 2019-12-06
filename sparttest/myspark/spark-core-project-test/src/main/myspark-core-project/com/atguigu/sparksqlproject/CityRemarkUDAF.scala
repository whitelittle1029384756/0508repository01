package com.atguigu.sparksqlproject


import java.text.DecimalFormat

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._


class CityRemarkUDAF extends UserDefinedAggregateFunction{
  override def inputSchema: StructType = StructType(StructField("city_name",StringType)::Nil)

  override def bufferSchema: StructType = StructType(StructField("city_count",MapType(StringType,LongType))::StructField("total",LongType)::Nil)

  override def dataType: DataType = StringType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0)=Map[String,Long]()
     buffer(1)=0L
  }
   //分区类聚合
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
       if(!input.isNullAt(0)){
           val cityName=input.getString(0)
          var cityCountMap=buffer.getMap[String,Long](0)
         cityCountMap+=cityName->(cityCountMap.getOrElse(cityName,0L)+1L)
          buffer(0)=cityCountMap
         buffer(1)=buffer.getLong(1)+1L   //更新点击总数
       }
  }
  //分区间的聚合
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      val map1=buffer1.getMap[String,Long](0)
      val map2=buffer2.getMap[String,Long](0)
      //将每种商品的value取出聚合 map1作为0值，
      val resultMap= map2.foldLeft(map1){
        //传入两个参数，迭代部分map和map2的元素元组，在处理
        {case (map,(cityName,count))=>map+(cityName->(map.getOrElse(cityName,0L)+count))}
      }
     buffer1(0)=resultMap
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)
  }
  //最后的返回值
  override def evaluate(buffer: Row): Any = {
      val   cityCountMap=buffer.getMap[String,Long](0)
       val  total=buffer.getLong(1)
      var top2List=cityCountMap.toList.sortBy(-_._2).take(2).map{
        case (cityName,count)=>CityRemark(cityName,count.toDouble/total)
      }
       top2List:+=CityRemark("其它",top2List.foldLeft(1D)((rate,cr)=>rate - cr.rate))
      top2List.mkString(",")
  }

}

case class CityRemark(cityName: String, rate: Double){
   val formater=new DecimalFormat(".00%")

  override def toString: String = s"$cityName:${formater.format(rate)}"
}