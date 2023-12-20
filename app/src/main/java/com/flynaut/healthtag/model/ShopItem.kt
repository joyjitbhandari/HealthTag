package com.flynaut.healthtag.model

import com.flynaut.healthtag.R

data class ShopItem (val imageResource: Int, val title: String, val price :String)

val shopItems = listOf(
    ShopItem(R.mipmap.ic_shop_item2, "BerryMed Pulse Oximeter", "$59.99"),
    ShopItem(R.mipmap.ic_shop_item1, "Omron 5 Series ® Wireless Blood Pressure Monitor", "$59.99"),
    ShopItem(R.mipmap.ic_shop_item1, "Omron 5 Series ® Wireless Blood Pressure Monitor", "$59.99"),
    ShopItem(R.mipmap.ic_shop_item1, "Omron 5 Series ® Wireless Blood Pressure Monitor", "$59.99")
)

val notificationItem = listOf(

    ShopItem(R.drawable.ic_chat, "Lorem Ipsum is simply dummy\ntext of the printing", "2 hours ago"),
    ShopItem(R.drawable.ic_bag, "Lorem Ipsum is simply dummy\ntext of the printing", "2 hours ago"),
    ShopItem(R.drawable.ic_chat, "Lorem Ipsum is simply dummy\ntext of the printing", "2 hours ago"),
    ShopItem(R.drawable.ic_bag, "Lorem Ipsum is simply dummy\ntext of the printing", "2 hours ago")

)



