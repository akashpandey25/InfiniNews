package com.project.infininews.repository

import androidx.room.Query
import com.project.infininews.api.RetrofitInstance
import com.project.infininews.db.ArticleDatabase
import com.project.infininews.model.Article
import java.util.Locale.IsoCountryCode

class NewsRepository(val db:ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        RetrofitInstance.api.searchNews(searchQuery , pageNumber)

    suspend fun insert(article: Article)=db.getArticleDao().insert(article)

    fun getFavoriteNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)


}