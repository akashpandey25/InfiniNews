package com.project.QuickNews.repository

import com.project.QuickNews.api.RetrofitInstance
import com.project.QuickNews.db.ArticleDatabase
import com.project.QuickNews.model.Article

class NewsRepository(val db:ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        RetrofitInstance.api.searchNews(searchQuery , pageNumber)

    suspend fun insert(article: Article)=db.getArticleDao().insert(article)

    fun getFavoriteNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)


}