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

    private val _articles = MediatorLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    init {
        val db = AppDatabase.getInstance(application)
        repository = ArticleRepository(db.articleDao())

        // React to changes in filters and order
        _articles.addSource(currentFilterText) { loadFilteredArticles() }
        _articles.addSource(currentFilterType) { loadFilteredArticles() }
        _articles.addSource(currentOrder) { loadFilteredArticles() }
    }

    private fun loadFilteredArticles() {
        val text = currentFilterText.value ?: ""
        val tipus = currentFilterType.value ?: "ALL"
        val order = currentOrder.value ?: "REFERENCIA"

        // Cargar artículos filtrados desde el repositorio
        viewModelScope.launch {
            repository.getFiltered(text, tipus, order).observeForever { filteredArticles ->
                _articles.value = filteredArticles
            }
        }
    }

    fun insert(article: Article) = viewModelScope.launch { repository.insert(article) }
    fun update(article: Article) = viewModelScope.launch { repository.update(article) }

    // Actualizamos la lista después de eliminar
    fun delete(article: Article) = viewModelScope.launch {
        repository.delete(article)
        loadFilteredArticles() // Recargar los artículos después de la eliminación
    }
}
