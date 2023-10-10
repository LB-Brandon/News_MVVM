package com.brandon.news_mvvm.respository

import androidx.lifecycle.ViewModel
import com.brandon.news_mvvm.api.RetrofitInstance
import com.brandon.news_mvvm.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) : ViewModel() {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

}