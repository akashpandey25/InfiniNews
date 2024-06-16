package com.project.infininews.utils

sealed class Resource<T> (  //it is used to see tha data has success or error
    val data:T?=null,
    val message: String?=null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?, data: T?=null): Resource<T>(data, message)
    class Loading<T>:Resource<T>()

}