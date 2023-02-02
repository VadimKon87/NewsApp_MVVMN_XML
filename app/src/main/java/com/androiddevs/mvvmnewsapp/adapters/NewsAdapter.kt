package com.androiddevs.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

// using DiffUtil instead of NotifyDataSetChanged

/**
* This code defines a class called NewsAdapter, which is a custom implementation of the
* RecyclerView.Adapter class. This adapter is used to display a list of articles in a RecyclerView.

* The class defines an inner class ArticleViewHolder which takes a single parameter, itemView of type
* View, and it's the constructor. It is a RecyclerView.ViewHolder class, which is used to hold the
* views for each item in the list.
*
* A ViewHolder is a pattern used in RecyclerView adapter in Android to improve the performance
* of the user interface by reducing the number of calls to findViewById().

* The ViewHolder holds the references to the views in the layout for each data item, so that the
* adapter can reuse these views as the user scrolls through the list, instead of calling
* findViewById() for every item in the list, which is slow. This significantly improves the
* scrolling performance of the RecyclerView.

* The private val differCallback is an object of DiffUtil.ItemCallback which is used to determine
* whether two items are the same and whether their contents are the same.

* The differ is an instance of AsyncListDiffer, which is used to calculate the difference between
* two lists of articles. It uses the differCallback for determining if the items are the same.

* The onCreateViewHolder method is used to inflate the view for each item in the list, and it
* returns an instance of ArticleViewHolder.

* The onBindViewHolder method is used to bind the data to the views for each item in the list. It
* takes an ArticleViewHolder and a position as a parameter, it loads the data of the article in the respective views.

* The getItemCount method returns the number of items in the list.

* setOnItemClickListener method takes a lambda function as a parameter and it is used to set a
* click listener on each item in the list.
* onItemClickListener is a nullable variable which is used to hold the lambda function that was
* passed to the setOnItemClickListener method.
* */

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        // usually we use id instead of url, but since we get items from our API,
        // they don't have id's, but they have unique url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    // the tool that compares our two lists, it will run in background (async)
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}