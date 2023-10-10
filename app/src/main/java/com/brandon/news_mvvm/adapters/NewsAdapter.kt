package com.brandon.news_mvvm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brandon.news_mvvm.R
import com.brandon.news_mvvm.databinding.ItemArticlePreviewBinding
import com.brandon.news_mvvm.models.Article
import com.bumptech.glide.Glide

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    val TAG = "NewsAdapter"
    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article) {
            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .into(binding.ivArticleImage)
            binding.tvSource.text = article.source?.name ?: ""
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt
            binding.root.setOnClickListener{
                Log.d(TAG, article.toString())
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val clickedArticle = differ.currentList[position]
                    onItemClickListener?.invoke(clickedArticle)
                }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview, parent, false)
        return ArticleViewHolder(ItemArticlePreviewBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}