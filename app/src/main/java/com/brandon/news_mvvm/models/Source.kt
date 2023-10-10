package com.brandon.news_mvvm.models


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    val id: Any? = null,
    @SerializedName("name")
    val name: String
)