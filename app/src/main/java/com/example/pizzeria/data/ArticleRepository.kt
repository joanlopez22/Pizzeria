package com.example.pizzeria.data

class ArticleRepository(private val articleDao: ArticleDao) {

    // Función que aplica los filtros de texto, tipo y orden a la consulta
    suspend fun getFiltered(text: String, type: String, order: String): List<Article> {
        return when {
            // Si no hay texto y tipo es "ALL", devuelve todos los artículos con el orden adecuado
            text.isEmpty() && type == "ALL" -> {
                if (order == "REFERENCIA") {
                    articleDao.getAllOrderedByReference()
                } else {
                    articleDao.getAllOrderedByDescription()
                }
            }
            // Si solo hay texto (y tipo es "ALL"), se filtra solo por texto
            text.isNotEmpty() && type == "ALL" -> {
                if (order == "REFERENCIA") {
                    articleDao.getFilteredOrderedByReference(text, "ALL")
                } else {
                    articleDao.getFilteredOrderedByDescription(text, "ALL")
                }
            }
            // Si solo hay tipo (y texto está vacío), se filtra solo por tipo
            type != "ALL" && text.isEmpty() -> {
                if (order == "REFERENCIA") {
                    articleDao.getFilteredOrderedByReference("", type)
                } else {
                    articleDao.getFilteredOrderedByDescription("", type)
                }
            }
            // Si ambos filtros están presentes, se filtra por ambos
            text.isNotEmpty() && type != "ALL" -> {
                if (order == "REFERENCIA") {
                    articleDao.getFilteredOrderedByReference(text, type)
                } else {
                    articleDao.getFilteredOrderedByDescription(text, type)
                }
            }
            // Si ambos filtros están vacíos, se muestran todos los artículos
            else -> {
                if (order == "REFERENCIA") {
                    articleDao.getAllOrderedByReference()
                } else {
                    articleDao.getAllOrderedByDescription()
                }
            }
        }
    }

    // Funciones para insertar, actualizar y eliminar artículos en la base de datos
    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }

    suspend fun update(article: Article) {
        articleDao.update(article)
    }

    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}