package com.atguigu.bean

case class UserVisitAction(
                            date: String,
                            user_id: Long,
                            session_id: String,
                            page_id: Long,
                            action_time: String,
                            search_keyword: String,
                            click_category_id: Long,
                            click_product_id: Long,
                            order_category_ids: String,
                            order_product_ids: String,
                            pay_category_ids: String,
                            pay_product_ids: String,
                            city_id: Long
                          ) {

}


case class CategoryCountInfo(categoryId: String,
                             clickCount: Long,
                             orderCount: Long,
                             payCount: Long)

case class CategorySession(categoryId:String,
                           sessionId:String,
                           clickCount:Long
                          ) extends  Ordered[CategorySession] {
  override def compare(that: CategorySession): Int = {
    if(this.clickCount <= that.clickCount) 1
    else -1
  }
}