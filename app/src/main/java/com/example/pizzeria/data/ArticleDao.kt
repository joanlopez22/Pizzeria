// data/ArticleDao.kt
package com.example.pizzeria.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE (:tipusFilter = 'ALL' OR tipus = :tipusFilter) AND descripcio LIKE '%' || :textFilter || '%' ORDER BY CASE WHEN :orderBy = 'REFERENCIA' THEN referencia ELSE descripcio END")
    fun getFilteredArticles(textFilter: String, tipusFilter: String, orderBy: String): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)
}