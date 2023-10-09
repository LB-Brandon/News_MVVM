package com.brandon.news_mvvm.ui

import androidx.lifecycle.ViewModel
import com.brandon.news_mvvm.respository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository

) : ViewModel() {

}