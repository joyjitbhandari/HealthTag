package com.flynaut.healthtag.model

import com.flynaut.healthtag.R

data class ChatItem (val imageResource: Int?, val msg: String, val time: String, val myMsg: Boolean)

val chatItems = listOf(
    ChatItem(R.mipmap.profile, "Lorem Ipsum is simply dummy text\nof the printing", "01:11 PM",true),
    ChatItem(null, "Hi there!", "01:10 PM",true),
    ChatItem(null, "Lorem Ipsum is simply", "01:10 PM", false),
    ChatItem(R.mipmap.avatar, "Lorem Ipsum is simply dummy text\nof the printing", "01:10 PM", false)
)
