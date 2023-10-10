package com.brandon.news_mvvm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandon.news_mvvm.R
import com.brandon.news_mvvm.adapters.NewsAdapter
import com.brandon.news_mvvm.databinding.FragmentBreakingNewsBinding
import com.brandon.news_mvvm.ui.NewsActivity
import com.brandon.news_mvvm.ui.NewsViewModel
import com.brandon.news_mvvm.util.Constants.Companion.QUERY_PAGE_SIZE
import com.brandon.news_mvvm.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    val TAG = "BreakingNewsFragment"
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            Log.d(TAG, it.toString())
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // RecyclerView의 레이아웃 매니저 가져오기 (LinearLayoutManager 사용)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            // 현재 화면에 보이는 첫 번째 아이템의 위치
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            // 현재 화면에 보이는 아이템의 개수
            val visibleItemCount = layoutManager.childCount

            // RecyclerView에 있는 아이템의 총 개수
            val totalItemCount = layoutManager.itemCount

            // 다음 페이지를 로드할 조건을 판단하는 변수들
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            // 다음 페이지를 로드해야 하는지 여부를 결정하는 변수
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            // shouldPaginate이 true인 경우 다음 페이지를 로드하고 스크롤 상태를 변경합니다.
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

    }



    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}