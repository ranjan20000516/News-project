package com.example.neapp.ui.model

import com.example.neapp.ui.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)