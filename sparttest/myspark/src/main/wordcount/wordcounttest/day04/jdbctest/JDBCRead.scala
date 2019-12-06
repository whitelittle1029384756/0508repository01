package wordcounttest.day04.jdbctest

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object JDBCRead {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //定义连接mysql的参数
      val driver="com.mysql.jdbc.Driver"
      val url="jdbc:mysql://hadoop104:3306/gmall"
        val userName="root"
        val password="123456"

      val rdd= new JdbcRDD(
          sc,
        ()=>{
           Class.forName(driver)
          DriverManager.getConnection(url,userName,password)
        },
        "select id,name from base_category1  where id >=? and id <=?",
         1,
            5,
             2,
                 //此处结果必须是元组（，）
        result=>{(result.getInt(1), result.getString(2))}
      )
  rdd.collect.foreach(println)
  }

}
