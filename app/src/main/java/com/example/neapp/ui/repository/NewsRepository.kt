package com.example.neapp.ui.repository

import com.example.neapp.ui.api.RetrofitInstance
import com.example.neapp.ui.db.ArticleDatabase
import com.example.neapp.ui.model.Article

class NewsRepository(private val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String,pageNumber: Int)= RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int)= RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) =db.getArticleDao().upsert(article)

    fun getSavedNews() =db.getArticleDao().getAllArticle()

    suspend fun deleteArticle(article: Article) =db.getArticleDao().deleteArticle(article)
}