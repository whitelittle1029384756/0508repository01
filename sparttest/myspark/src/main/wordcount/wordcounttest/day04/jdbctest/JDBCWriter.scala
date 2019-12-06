package wordcounttest.day04.jdbctest

import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.JdbcRDD

object JDBCWriter {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //定义连接mysql的参数
    val driver="com.mysql.jdbc.Driver"
    val url="jdbc:mysql://hadoop104:3306/db1"
    val userName="root"
    val password="123456"
     val rdd=sc.parallelize(Array((1,("liubei",18)),(2,("zhangfei",17))))
     rdd.foreachPartition(it=>{
         Class.forName(driver)
         val conn=DriverManager.getConnection(url,userName,password)
         it.foreach(x =>{
              val statement=conn.prepareStatement("insert into stu2 value(?,?,?)")
                     statement.setInt(1,x._1)
                     statement.setString(2,x._2._1)
               statement.setInt(3,x._2._2)
            statement.executeUpdate()
              statement.close()
         })
         conn.close()
     })
    rdd.collect.foreach(println)
  }
}
