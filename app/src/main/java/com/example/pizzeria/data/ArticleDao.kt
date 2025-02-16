package com.example.pizzeria.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ArticleDao {

    @Insert
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)

    // Obtener todos los artículos ordenados por referencia
    @Query("SELECT * FROM articles ORDER BY referencia ASC")
    fun getAllOrderedByReference(): LiveData<List<Article>>

    // Obtener todos los artículos ordenados por descripción
    @Query("SELECT * FROM articles ORDER BY descripcio ASC")
    fun getAllOrderedByDescription(): LiveData<List<Article>>

    // Filtrar los artículos por descripción y tipo, y ordenarlos por referencia
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY referencia ASC")
    fun getFilteredOrderedByReference(text: String, type: String): LiveData<List<Article>>

    // Filtrar los artículos por descripción y tipo, y ordenarlos por descripción
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY descripcio ASC")
    fun getFilteredOrderedByDescription(text: String, type: String): LiveData<List<Article>>

    // Filtrar los artículos por solo tipo, ordenados por referencia
    @Query("SELECT * FROM articles WHERE tipus LIKE :type ORDER BY referencia ASC")
    fun getFilteredByTypeOrderedByReference(type: String): LiveData<List<Article>>

    // Filtrar los artículos por solo tipo, ordenados por descripción
    @Query("SELECT * FROM articles WHERE tipus LIKE :type ORDER BY descripcio ASC")
    fun getFilteredByTypeOrderedByDescription(type: String): LiveData<List<Article>>

    // Filtrar los artículos por solo texto (descripción), ordenados por referencia
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text ORDER BY referencia ASC")
    fun getFilteredByTextOrderedByReference(text: String): LiveData<List<Article>>

    // Filtrar los artículos por solo texto (descripción), ordenados por descripción
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text ORDER BY descripcio ASC")
    fun getFilteredByTextOrderedByDescription(text: String): LiveData<List<Article>>
}
