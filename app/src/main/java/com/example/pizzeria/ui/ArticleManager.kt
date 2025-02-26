package com.example.pizzeria.ui

import android.app.Application
import android.content.Context
import com.example.pizzeria.data.AppDatabase
import com.example.pizzeria.data.Article
import com.example.pizzeria.data.ArticleRepository
import kotlinx.coroutines.runBlocking

class ArticleManager(private val application: Application) {
    private val repository: ArticleRepository
    var currentFilterText: String = ""
    var currentFilterType: String = "ALL"
    var currentOrder: String = "REFERENCIA"
    var ivaPercentage: Float

    init {
        val db = AppDatabase.getInstance(application)
        repository = ArticleRepository(db.articleDao())

        // Cargar IVA desde SharedPreferences
        val prefs = application.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
        ivaPercentage = prefs.getFloat("ivaPercentage", 21f)
    }

    // Obtener la lista de artículos filtrados
    fun getFilteredArticles(): List<Article> {
        // Usamos runBlocking para operaciones de base de datos en un contexto simple
        return runBlocking {
            repository.getFiltered(
                currentFilterText,
                currentFilterType,
                currentOrder
            )
        }
    }

    // Calcular precio con IVA
    fun calculatePriceWithIva(priceWithoutIva: Float): Float {
        return priceWithoutIva * (1 + ivaPercentage / 100)
    }

    // Operaciones CRUD
    fun insert(article: Article) {
        runBlocking {
            repository.insert(article)
        }
    }

    fun update(article: Article) {
        runBlocking {
            repository.update(article)
        }
    }

    fun delete(article: Article) {
        runBlocking {
            repository.delete(article)
        }
    }
}