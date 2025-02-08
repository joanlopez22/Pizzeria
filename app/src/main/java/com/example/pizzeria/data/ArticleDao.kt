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

    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY referencia ASC")
    fun getFilteredOrderedByReference(text: String, type: String): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE descripcio LIKE :text AND tipus LIKE :type ORDER BY descripcio ASC")
    fun getFilteredOrderedByDescription(text: String, type: String): LiveData<List<Article>>
}
