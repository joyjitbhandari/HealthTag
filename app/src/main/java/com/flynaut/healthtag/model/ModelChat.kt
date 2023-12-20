package com.flynaut.healthtag.model

data class ModelChat (
    var sender: String? = null,
    var receiver: String? = null,
    var text: String? = null,
    var type: String? = null,
    var timeStamp: String? = null
)