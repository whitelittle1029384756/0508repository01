package com.atguigu.spark_sql.day02.jdbc

import java.util.Properties

import com.atguigu.spark_sql.day01.User
import org.apache.spark.sql.{SaveMode, SparkSession}
//import org.spark_project.jetty.server.Authentication.User

object JDBCWrite {
  def main(args: Array[String]): Unit = {
    val spark=SparkSession.builder()
      .master("local[2]")
      .appName("JDBCDemo")
      .getOrCreate()
    //导入隐式转换
    import spark.implicits._

    val rdd=spark.sparkContext.parallelize(Array(User("zl",25,"femal"),User("ws",20,"femal")))
        val ds=rdd.toDS
//        ds.write
//      .format("jdbc")
//      .option("url","jdbc:mysql://hadoop104:3306/db1")
//      .option("user","root")
//      .option("password","123456")
//      .option("dbtable","stu3")
//
//      .save()
      val props=new Properties()
      props.setProperty("user","root")
     props.setProperty("password","123456")
       ds.write
         .mode(SaveMode.Append).jdbc("jdbc:mysql://hadoop104:3306/db1","stu3",props)


    ds.show()
      spark.close()
  }
}
