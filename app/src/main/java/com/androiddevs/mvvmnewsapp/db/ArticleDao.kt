package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long // returns PrimaryKey number

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>
    // whenever an article inside this list changes, LiveData will notify all its' observers that
    // subscribed to changes of that LiveData. very useful for rotating device
    // suspend fun does not work with LiveData

    @Delete
    suspend fun deleteArticle(article: Article)

}