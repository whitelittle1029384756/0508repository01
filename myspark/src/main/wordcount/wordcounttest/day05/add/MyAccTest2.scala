package wordcounttest.day05.add

import org.apache.spark.util.AccumulatorV2

class MyAccTest2 extends AccumulatorV2[Long,Map[String,Double]]{
     var count=0L
    var map=Map[String,Double]()
  override def isZero: Boolean = count==0L&&map.isEmpty

  override def copy(): AccumulatorV2[Long, Map[String, Double]] = {
     var acc= new MyAccTest2
        acc.map=map
         acc.count=count
        acc
  }

  override def reset(): Unit = {
        count=0
       map=Map[String,Double]()
  }

  override def add(v: Long): Unit = {
          count+=1
        map+=("sum"->(map.getOrElse("sum",0D)+v))
       map+= "max"->map.getOrElse("max",Long.MinValue.toDouble).max(v)
       map+= "min"->map.getOrElse("min",Long.MaxValue.toDouble).min(v)

  }

  override def merge(other: AccumulatorV2[Long, Map[String, Double]]): Unit = {
//    case o: MyAccTest2 =>
//      count += o.count
//      this.map += "sum" -> (this.map.getOrElse("sum", 0D) += o.map.getOrElse("sum", 0D))
//      this.map += "max" -> this.map.getOrElse("max", Long.MinValue.toDouble).max(o.map.getOrElse("max", Long.MinValue.toDouble))
//      this.map += "min" -> this.map.getOrElse("min", Long.MaxValue.toDouble).min(o.map.getOrElse("min", Long.MaxValue.toDouble))
//
//
//    case _ => throw new UnsupportedOperationException
  }


  override def value: Map[String, Double] = {
    this.map += "avg" -> this.map.getOrElse("sum", 0D) / this.count
    this.map
  }
}
