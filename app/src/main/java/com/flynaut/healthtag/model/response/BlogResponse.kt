package com.flynaut.healthtag.model.response

data class BlogResponse(
    val status: Int,
    val message: String,
    val `data`: List<BlogDetails>,
)

data class BlogDetails(
    val _id: String,
    val category_name: String,
    val images: List<String>,
    val title: String,
    val description: String,
    val __v: Int
)
