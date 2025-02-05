// ui/ArticleViewModel.kt
package com.example.pizzeria.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.pizzeria.data.AppDatabase
import com.example.pizzeria.data.Article
import com.example.pizzeria.data.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleRepository
    val currentFilterText = MutableLiveData("")
    val currentFilterType = MutableLiveData("ALL")
    val currentOrder = MutableLiveData("REFERENCIA")

    val articles: LiveData<List<Article>> = Transformations.switchMap(
        currentFilterText.zip(currentFilterType).zip(currentOrder)
    ) { (filters, order) ->
        repository.getFiltered(filters.first, filters.second, order)
    }

    init {
        val db = AppDatabase.getInstance(application)
        repository = ArticleRepository(db.articleDao())
    }

    fun insert(article: Article) = viewModelScope.launch { repository.insert(article) }
    fun update(article: Article) = viewModelScope.launch { repository.update(article) }
    fun delete(article: Article) = viewModelScope.launch { repository.delete(article) }
}