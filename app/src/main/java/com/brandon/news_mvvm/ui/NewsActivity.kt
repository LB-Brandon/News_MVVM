package com.brandon.news_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.brandon.news_mvvm.R
import com.brandon.news_mvvm.databinding.ActivityNewsBinding
import com.brandon.news_mvvm.db.ArticleDatabase
import com.brandon.news_mvvm.respository.NewsRepository

class NewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =  ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())




    }
}