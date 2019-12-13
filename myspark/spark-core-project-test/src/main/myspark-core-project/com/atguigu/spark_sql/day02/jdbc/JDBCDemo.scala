package com.atguigu.spark_sql.day02.jdbc

import java.util.Properties

import org.apache.spark.sql.SparkSession

object JDBCDemo {
  def main(args: Array[String]): Unit = {
    // 初始化sparkSession
    val spark=SparkSession.builder()
      .master("local[2]")
      .appName("JDBCDemo")
      .getOrCreate()
    //导入隐式转换
    import spark.implicits._
     //普通的的读
/*     val df= spark.read.format("jdbc")
       .option("url","jdbc:mysql://hadoop104:3306/db1")
       .option("user","root")
       .option("password","123456")
       .option("dbtable","stu2")
       .load()*/

    //专用的读
    val props= new Properties()
        props.setProperty("user","root")
    props.setProperty("password","123456")
      val df=spark.read.jdbc("jdbc:mysql://hadoop104:3306/db1","stu2",props)
    df.show()

     spark.close()

  }
}
