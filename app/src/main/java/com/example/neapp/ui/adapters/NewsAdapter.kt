package com.example.neapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neapp.R
import com.example.neapp.ui.model.Article
import kotlinx.android.synthetic.main.item_article.view.*

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

     private val diffCallBack = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ= AsyncListDiffer(this,diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
       val currentArticle= differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentArticle.urlToImage).into(ivArticleImage)
            tvSource.text= currentArticle.source?.name
            tvPublishedAt.text= currentArticle.publishedAt
            tvTitle.text= currentArticle.title
            tvDescription.text= currentArticle.description
            setOnClickListener{
                onItemClickedListener?.let { it(currentArticle) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickedListener: ((Article) -> Unit)? = null

    fun setOnItemClickedListener(listener: (Article) -> Unit){
        onItemClickedListener = listener
    }
}