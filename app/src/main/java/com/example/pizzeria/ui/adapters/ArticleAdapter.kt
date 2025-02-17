package com.example.pizzeria.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeria.R
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ItemArticleBinding
import com.example.pizzeria.ui.ArticleViewModel

class ArticleAdapter(
    private val context: Context,
    private val onItemClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ViewHolder>(DiffCallback()) {

    private val viewModel: ArticleViewModel = ViewModelProvider(context as androidx.appcompat.app.AppCompatActivity).get(ArticleViewModel::class.java)

    inner class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.referencia == newItem.referencia
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)

        // Obtener el precio con IVA
        val priceWithIva = viewModel.calculatePriceWithIva(article.preuSenseIva)

        with(holder.binding) {
            root.setBackgroundColor(getColorForType(article.tipus))
            tvReference.text = article.referencia
            tvDescription.text = article.descripcio

            // Mostrar el precio con IVA
            tvPrice.text = "%.2f€".format(priceWithIva)

            btnDelete.setOnClickListener { onDeleteClick(article) }
            root.setOnClickListener { onItemClick(article) }
        }
    }

    private fun getColorForType(type: String): Int {
        return when (type) {
            Article.TIPUS_PIZZA -> ContextCompat.getColor(context, R.color.color_pizza)
            Article.TIPUS_VEGANA -> ContextCompat.getColor(context, R.color.color_vegana)
            Article.TIPUS_CELIACA -> ContextCompat.getColor(context, R.color.color_celiaca)
            Article.TIPUS_TOPPING -> ContextCompat.getColor(context, R.color.color_topping)
            else -> Color.WHITE
        }
    }
}
