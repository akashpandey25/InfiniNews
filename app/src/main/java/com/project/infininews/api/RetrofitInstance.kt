package com.project.infininews.api

import com.google.gson.GsonBuilder
import com.project.infininews.utils.Contents.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{

        private val retrofit by lazy {
            val logging=HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client=OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //sets okhttp client
                .build()
        }
        val api by lazy {
            retrofit.create(NewsApi::class.java) //it creates the implementation of new api interface using retrofit instance
        }
    }
}