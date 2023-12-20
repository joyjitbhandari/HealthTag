package com.flynaut.healthtag.model

data class CategoryItem (val imageResource: String, val title: String, val description: String)

val blogItems = mutableListOf<CategoryItem>()


//val items = listOf(
//    CategoryItem(R.mipmap.category1, "OSA"),
//    CategoryItem(R.mipmap.copd, "COPD"),
//    CategoryItem(R.mipmap.asthma, "Asthma"),
//    CategoryItem(R.mipmap.diabetes, "Diabetes"),
//    CategoryItem(R.mipmap.weight, "Weight Management"),
//    CategoryItem(R.mipmap.hypertension, "HyperTension"),
//    CategoryItem(R.mipmap.aging, "Aging")
//)
//val blogItems = listOf(
//    CategoryItem(R.mipmap.blog_img1, "Lorem Ipsum is simply dummy text of the printing"),
//    CategoryItem(R.mipmap.blog_img2, "Lorem Ipsum is simply dummy text of the printing"),
//    CategoryItem(R.mipmap.blog_img3, "Lorem Ipsum is simply dummy text of the printing")
//)
//val ticketItems = listOf(
//    CategoryItem(R.drawable.ic_ticket1, "Solved"),
//    CategoryItem(R.drawable.ic_ticket2, "Open"),
//    CategoryItem(R.drawable.ic_ticket, "Solved"),
//    CategoryItem(R.drawable.ic_ticket1, "Solved")
//)