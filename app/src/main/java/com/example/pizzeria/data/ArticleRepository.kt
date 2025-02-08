package com.example.pizzeria.data

import androidx.lifecycle.LiveData

class ArticleRepository(private val articleDao: ArticleDao) {

    fun getFiltered(text: String, type: String, order: String): LiveData<List<Article>> {
        return when (order) {
            "DESCRIPCIO" -> articleDao.getFilteredOrderedByDescription(text, type)
            else -> articleDao.getFilteredOrderedByReference(text, type)
        }
    }

    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }

    suspend fun update(article: Article) {
        articleDao.update(article)
    }

    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}
