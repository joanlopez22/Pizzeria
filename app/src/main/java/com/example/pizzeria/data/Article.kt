// data/Article.kt
package com.example.pizzeria.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val referencia: String,
    val descripcio: String,
    val tipus: String,
    @ColumnInfo(name = "preu_sense_iva") val preuSenseIva: Float
) : Parcelable {
    companion object {
        const val TIPUS_PIZZA = "PIZZA"
        const val TIPUS_VEGANA = "PIZZA VEGANA"
        const val TIPUS_CELIACA = "PIZZA CELIACA"
        const val TIPUS_TOPPING = "TOPPING"
    }
}
