package com.androiddevs.mvvmnewsapp.models

data class NewsResponse(
    val articles: MutableList<Article>, //step 11.2 changed from list to mutable list
    val status: String,
    val totalResults: Int
)