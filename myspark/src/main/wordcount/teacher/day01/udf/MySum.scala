package teacher.day01.udf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}
/*
sum(salary)
 */
class MySum extends UserDefinedAggregateFunction{
    // 定义聚合函数输入数据的类型    Double ...
    override def inputSchema: StructType = StructType(StructField("column", DoubleType):: Nil)
    
    // 缓冲区的数据类型
    override def bufferSchema: StructType = StructType(StructField("sum", DoubleType):: Nil)
    
    // 最终的返回值类型
    override def dataType: DataType = DoubleType
    
    // 确定性: 相同输入是否应该返回相同的输出
    override def deterministic: Boolean = true
    
    // 初始化: 定义缓冲区的零值
    override def initialize(buffer: MutableAggregationBuffer): Unit = buffer(0) = 0d
    
    
    // 分区的聚合
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        buffer(0) = buffer.getDouble(0) + input.getAs[Double](0)
    }
    
    // 分区间的聚合
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(0) = buffer1.getDouble(0) + buffer2.getAs[Double](0)
    }
    
    // 返回最终的值
    override def evaluate(buffer: Row): Double =buffer.getDouble(0)
}
