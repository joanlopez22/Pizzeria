package com.example.pizzeria.data

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
    suspend fun getAllOrderedByReference(): List<Article>

    // Obtener todos los artículos ordenados por descripción
    @Query("SELECT * FROM articles ORDER BY descripcio ASC")
    suspend fun getAllOrderedByDescription(): List<Article>

    // Filtrar los artículos por descripción y tipo, y ordenarlos por referencia
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY referencia ASC")
    suspend fun getFilteredOrderedByReference(text: String, type: String): List<Article>

    // Filtrar los artículos por descripción y tipo, y ordenarlos por descripción
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY descripcio ASC")
    suspend fun getFilteredOrderedByDescription(text: String, type: String): List<Article>

    // Filtrar los artículos por solo tipo, ordenados por referencia
    @Query("SELECT * FROM articles WHERE tipus LIKE :type ORDER BY referencia ASC")
    suspend fun getFilteredByTypeOrderedByReference(type: String): List<Article>

    // Filtrar los artículos por solo tipo, ordenados por descripción
    @Query("SELECT * FROM articles WHERE tipus LIKE :type ORDER BY descripcio ASC")
    suspend fun getFilteredByTypeOrderedByDescription(type: String): List<Article>

    // Filtrar los artículos por solo texto (descripción), ordenados por referencia
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text ORDER BY referencia ASC")
    suspend fun getFilteredByTextOrderedByReference(text: String): List<Article>

    // Filtrar los artículos por solo texto (descripción), ordenados por descripción
    @Query("SELECT * FROM articles WHERE descripcio LIKE :text ORDER BY descripcio ASC")
    suspend fun getFilteredByTextOrderedByDescription(text: String): List<Article>
}