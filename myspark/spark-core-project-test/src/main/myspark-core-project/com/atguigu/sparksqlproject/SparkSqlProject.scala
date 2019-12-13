package com.atguigu.sparksqlproject

import com.atguigu.sparksqlproject2.CityRemarkUDAF2
import org.apache.spark
import org.apache.spark.sql.SparkSession

object SparkSqlProject {
  def main(args: Array[String]): Unit = {
    val spark  = SparkSession
      .builder()
      .master("local[*]")
      .appName("SqlPractice")
      .enableHiveSupport()
      .getOrCreate()

    //注册
    spark.udf.register("remark",new CityRemarkUDAF2)
    spark.sql("use spark_sql")

    spark.sql(
      """select
        |c.*,
        |u.click_product_id,
        | p.product_name
        | from
        | user_visit_action u join
        | product_info p  join city_info c
        | on u.city_id = c.city_id and u.click_product_id=p.product_id
        | where u.click_product_id>-1
      """.stripMargin).createOrReplaceTempView("t1")

    spark.sql(
      """  select
        |       t1.area,
        |       t1.product_name,
        |     count(*) click_count,
        |     remark(city_name) remark
        |     from t1
        |      group by area,product_name
      """.stripMargin).createOrReplaceTempView("t2")

      spark.sql(
        """
          |  select
          |   *,
          |     row_number() over(partition by area order by click_count desc) rank
          |     from t2
        """.stripMargin).createOrReplaceTempView("t3")
      spark.sql(
        """
          |    select
          |   area,
          |   product_name,
          |   click_count,
          |   remark
          |    from t3
          |    where rank <=3
        """.stripMargin).show(10,false)

       spark.sql(
         """
           |
           |select
           | area,
           | product_name,
           | click_count,
           | rank
           | from
           |
           |(select
           |  area,
           |  product_name,
           |  click_count,
           |  row_number() over(partition by area order by click_count desc) rank
           | from
           |(select
           |  t1.area,
           |   t1.product_name,
           |  count(*) click_count
           | from
           | (select
           |  c.*,
           |  p.product_name,
           |  u.click_product_id
           | from user_visit_action u join
           | product_info p on u.click_product_id=p.product_id
           | join city_info c on u.city_id=c.city_id) t1
           |   group by t1.area,t1.product_name  ) t2 ) t3
           |   where rank <=3
         """.stripMargin).show(10)
    spark.close()

  }
}
