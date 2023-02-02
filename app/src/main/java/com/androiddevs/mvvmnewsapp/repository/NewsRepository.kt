package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article

class NewsRepository(
    val db: ArticleDatabase?
) {
    // step 7.1
    // because network function is suspend function, this function is also suspend fun
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    // 8.1
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    // 10.1 (in tutorial safe call were not needed)
    suspend fun upsert(article: Article) = db?.getArticleDao()?.upsert(article)

    fun getSavedNews() = db?.getArticleDao()?.getAllArticles() //returns LiveData, so it cannot be suspend fun

    suspend fun deleteArticle(article: Article) = db?.getArticleDao()?.deleteArticle(article)
}