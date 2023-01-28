package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofit by lazy {

            // to log responses of retrofit, useful for debugging
            val logging = HttpLoggingInterceptor()
            logging.setLevel((HttpLoggingInterceptor.Level.BODY))

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            /* End of LoggingInterceptor code*/

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // converts JSON API response to kotlin objects
                .client(client) // logging interceptor
                .build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}