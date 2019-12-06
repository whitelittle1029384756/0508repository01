package wordcounttest.day04.hbasetest

import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

object HbaseReaderTest {
  def main(args: Array[String]): Unit = {
       val conf=new SparkConf().setAppName("Practice").setMaster("local[2]")
        val sc = new SparkContext(conf)
         //设置配置
      val hbaseConf= HBaseConfiguration.create()
           hbaseConf.set("hbase.zookeeper.quorum","hadoop104,hadoop105,hadoop106")
        hbaseConf.set(TableInputFormat.INPUT_TABLE,"student")

    val  rdd=sc.newAPIHadoopRDD(
      hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result] )
/*    val rdd2 = rdd1.map{
      case (key, result) => {
        //                Bytes.toString(result.getRow)
        //                Bytes.toString(key.get())
        val cells= result.listCells()
        // 带入隐式转换, 内置了很多java和scala集合互转的方法
        import scala.collection.JavaConversions._
        for (cell <- cells) {
          println(Bytes.toString(CellUtil.cloneQualifier(cell)))
        }
      }
    }*/
       val rdd2=rdd.map{
          case(key,result) =>{
            val cells=result.listCells()
            //带入隐式转换，内置很多java和scala集合互转的方法
            import scala.collection.JavaConversions._
            for(cell<-cells){
              println(Bytes.toString(CellUtil.cloneQualifier(cell)))
            }
          }
       }
    rdd2.collect.foreach(println)
    sc.stop()

  }
}
