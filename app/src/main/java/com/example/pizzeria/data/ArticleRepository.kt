// data/ArticleRepository.kt
package com.example.pizzeria.data

import androidx.lifecycle.LiveData

class ArticleRepository(private val articleDao: ArticleDao) {
    fun getFiltered(text: String, tipus: String, order: String): LiveData<List<Article>> {
        return articleDao.getFilteredArticles(text, tipus, order)
    }

    suspend fun insert(article: Article) = articleDao.insert(article)
    suspend fun update(article: Article) = articleDao.update(article)
    suspend fun delete(article: Article) = articleDao.delete(article)
}