package com.brandon.news_mvvm.respository

import androidx.lifecycle.ViewModel
import com.brandon.news_mvvm.api.RetrofitInstance
import com.brandon.news_mvvm.db.ArticleDatabase
import com.brandon.news_mvvm.models.Article

class NewsRepository(
    val db: ArticleDatabase
) : ViewModel() {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)


}