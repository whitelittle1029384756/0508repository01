package wordcounttest.day05.add

import org.apache.spark.util.AccumulatorV2

object MyAccTest {

}

class MyAccTest extends AccumulatorV2[Long,Long]{
    //缓存中间值
  var sum=0L
  //判断零值：初始值
  override def isZero: Boolean = sum==0
 //复制累加器
  override def copy(): AccumulatorV2[Long, Long] = {
       val newAcc=new MyAccTest
        newAcc.sum = sum
     newAcc
  }
//重置累加器
  override def reset(): Unit = sum = 0
//累加
  override def add(v: Long): Unit =sum+=v
  //合并：合并累加器
  override def merge(other: AccumulatorV2[Long, Long]): Unit = {
    other match{
      case  o:MyAccTest=>sum+=o.sum
      case _=>throw new IllegalStateException
    }
  }
  //返回最终值的结果
  override def value: Long = this.sum
}
