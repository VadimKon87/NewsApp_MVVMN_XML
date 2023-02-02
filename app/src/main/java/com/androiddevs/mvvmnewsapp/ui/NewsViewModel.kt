package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    // step 7.2

    // because we use LiveData our fragments will automatically be notified of changes, which is very
    // useful for rotating device
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    //step 11.1.1
    var breakingNewsResponse: NewsResponse? = null

    // 8.2
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    //step 11.1.1
    var searchNewsResponse: NewsResponse? = null

    // step 7.4
    init {
        getBreakingNews("ca")
    }

    //7.2
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading()) // here we change fragment (livedata will update)
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response)) // and here we change view
    }
    // 7.2 end

    //8.2
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }
    //8.2 end

    //7.2
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
    // 11.1.2
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles) // line added after step 11.2
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
                // return Resource.Success(resultResponse) - before step 11 this was return
                // end 11.1.2
            }
        }
        return Resource.Error(response.message())
    }
    /* ********** end of 7.2 for this file *********** */

    // step 8.2
    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // 11.1.2
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles) // line added after step 11.2
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
                // return Resource.Success(resultResponse) - before step 11 this was return
                // end 11.1.2
            }
        }
        return Resource.Error(response.message())
    }

    // step 10.2
    // for suspend functions we launch coroutine
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}