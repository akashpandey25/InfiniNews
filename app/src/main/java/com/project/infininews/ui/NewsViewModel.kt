package com.project.infininews.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.infininews.model.Article
import com.project.infininews.model.NewsResponse
import com.project.infininews.repository.NewsRepository
import com.project.infininews.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import retrofit2.http.Query
import java.lang.Error


class NewsViewModel(app:Application, val newsRepository:NewsRepository):AndroidViewModel(app) {

    val headlines:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage=1
    var headlinesResponse:NewsResponse?=null

    val searchNews:MutableLiveData<Resource<NewsResponse>?> =MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null

    var newSearchQuery:String?=null
    var oldSearchQuery:String?=null

    init {
        getHeadlines("in")
    }

    fun getHeadlines(countryCode: String)=viewModelScope.launch{
            headlinesInternet(countryCode)
    }
    fun searchNews(searchQuery: String)=viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }
    //it handles headlines, pages
    private  fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
            if(response.isSuccessful){
                response.body()?.let { resultResponse->
                    headlinesPage++
                    if(headlinesResponse==null){
                        headlinesResponse=resultResponse
                    }
                    else{
                        val oldArticles=headlinesResponse?.articles
                        val newArticles=resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return  Resource.Success(headlinesResponse?:resultResponse)
                }
            }
        return Resource.Error(response.message())
    }
    //it handles the search news pages and query
    private fun handleNewsSearchResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                if(searchNewsResponse==null || newSearchQuery != oldSearchQuery){
                    searchNewsPage=1
                    oldSearchQuery=newSearchQuery
                    searchNewsResponse=resultResponse
                }
                else{
                    searchNewsPage++
                    val oldArticle=searchNewsResponse?.articles
                    val newArticle=resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return  Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavorite(article: Article)=viewModelScope.launch {
        newsRepository.insert(article)
    }
    fun getFavoriteNews()=newsRepository.getFavoriteNews()

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    //this function for checking and getting internet permission of the device
    fun interNetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return  getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                    else->false
                }
            }?:false
        }
    }

    //it fetches headlines based on the country code and errors also
    private suspend fun headlinesInternet(countryCode: String){
        headlines.postValue(Resource.Loading()) //it shows loading that network progress is in progress
        try{ //shows headlines when the internet is on
            if(interNetConnection(this.getApplication())){  //here checks for internet connection
                val response=newsRepository.getHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleHeadlinesResponse(response))
            }else{ //when internet is off shows it
                headlines.postValue(Resource.Error("No Internet"))
            }
        }catch (t:Throwable){ //if any exception or anything will got catch in it
            when(t){
                is IOException->headlines.postValue(Resource.Error("Unable To Connect"))
                else->headlines.postValue(Resource.Error("No Signal"))
            }
        }
    }
    private suspend fun searchNewsInternet(searchQuery: String){
            newSearchQuery=searchQuery
        searchNews.postValue(Resource.Loading())
        try{
            if(interNetConnection(this.getApplication())){
                val response=newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleNewsSearchResponse(response))
            }else{
                searchNews.postValue((Resource.Error("No Internet")))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->searchNews.postValue(Resource.Error("Unable To Connect"))
                else-> searchNews.postValue(Resource.Error("No Signal"))
            }
        }
    }
}