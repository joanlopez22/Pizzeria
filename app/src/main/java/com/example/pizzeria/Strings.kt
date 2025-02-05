// Strings.kt
package com.example.pizzeria

import android.content.Context
import androidx.preference.PreferenceManager

object Strings {
    const val FILTER_ANY_TYPE = "ALL"
    const val ORDER_BY_REFERENCE = "REFERENCIA"
    const val ORDER_BY_DESCRIPTION = "DESCRIPCIO"
}// Añadir en Strings.kt
const val PREF_IVA = "iva_key"

// En cualquier lugar donde calcules el precio con IVA
fun calculatePreuAmbIVA(preuSenseIVA: Float, context: Context): String {
    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    val iva = sharedPrefs.getFloat(PREF_IVA, 0.10f) // 10% por defecto
    return "%.2f€".format(preuSenseIVA * (1 + iva))
}