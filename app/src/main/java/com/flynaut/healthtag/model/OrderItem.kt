package com.flynaut.healthtag.model

import com.flynaut.healthtag.R

data class OrderItem (val imageResource: Int, val title: String, val price :Double, var totalQty: Int, val orderStatus: String,val time: String, val rating: Int?)

val orderItems = listOf(
    OrderItem(R.mipmap.ic_cart_product1, "Omron 5 Series ® Wireless Blood Pressure Monitor", 49.99,8,"Placed","14th March 2020 at 7.20 pm",null ),
    OrderItem(R.mipmap.ic_cart_product, "Omron 7 Series ® Wireless Upper Arm Blood Pressure Monitor", 49.99,8,"Delivered","14th March 2020 at 7.20 pm",4 ),
    OrderItem(R.mipmap.ic_cart_product, "Omron 7 Series ® Wireless Upper Arm Blood Pressure Monitor", 49.99,8,"Delivered","14th March 2020 at 7.20 pm",null ),
    OrderItem(R.mipmap.ic_cart_product, "Omron 7 Series ® Wireless Upper Arm Blood Pressure Monitor", 49.99,8,"Cancelled","14th March 2020 at 7.20 pm",null )
)



