package com.ashwin.android.currencyconverter.util

sealed class Resource<T>(val data: T? = null, val message: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(message: String) : Resource<T>(null, message)
}