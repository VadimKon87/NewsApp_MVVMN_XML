package com.androiddevs.mvvmnewsapp.util

/*
* sealed class is similar to abstract class, but we define what classes are allowed to inherit from it.
* in our example we have 3 classes that will inherit from Resource class
* */

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    // T not nullable because if it's successful, then it has a body (data)
    class Success<T>(data: T) : Resource<T>(data)

    // will have error message, so not null, but maybe no data
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    class Loading<T> : Resource<T>()
}