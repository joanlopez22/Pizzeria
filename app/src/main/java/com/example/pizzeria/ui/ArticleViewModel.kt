package com.example.pizzeria.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.pizzeria.data.AppDatabase
import com.example.pizzeria.data.Article
import com.example.pizzeria.data.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ArticleRepository
    val currentFilterText = MutableLiveData<String>()
    val currentFilterType = MutableLiveData<String>()
    val currentOrder = MutableLiveData<String>()

    private val _articles = MediatorLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    init {
        val db = AppDatabase.getInstance(application)
        repository = ArticleRepository(db.articleDao())

        // Inicializamos los filtros con valores predeterminados
        currentFilterText.value = ""
        currentFilterType.value = "ALL"
        currentOrder.value = "REFERENCIA"

        // Reaccionamos a los cambios en los filtros
        _articles.addSource(currentFilterText) { loadFilteredArticles() }
        _articles.addSource(currentFilterType) { loadFilteredArticles() }
        _articles.addSource(currentOrder) { loadFilteredArticles() }
    }

    // Cargar los artículos según los filtros
    private fun loadFilteredArticles() {
        val text = currentFilterText.value ?: ""
        val type = currentFilterType.value ?: "ALL"
        val order = currentOrder.value ?: "REFERENCIA"

        // La función ahora devuelve un LiveData, por lo que no es necesario usar launch
        _articles.addSource(repository.getFiltered(text, type, order)) { articles ->
            _articles.value = articles  // Asignamos directamente los artículos filtrados
        }
    }

    fun insert(article: Article) = viewModelScope.launch {
        repository.insert(article)
        loadFilteredArticles() // Recargar los artículos después de insertar
    }

    fun update(article: Article) = viewModelScope.launch {
        repository.update(article)
        loadFilteredArticles() // Recargar los artículos después de actualizar
    }

    fun delete(article: Article) = viewModelScope.launch {
        repository.delete(article)
        loadFilteredArticles() // Recargar los artículos después de eliminar
    }
}
