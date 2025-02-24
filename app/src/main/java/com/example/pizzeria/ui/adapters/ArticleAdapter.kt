package com.example.pizzeria.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeria.R
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ItemArticleBinding
import com.example.pizzeria.ui.ArticleViewModel
import androidx.lifecycle.ViewModelProvider

class ArticleAdapter(
    private val context: Context,
    private val onItemClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ViewHolder>(DiffCallback()) {

    private val viewModel: ArticleViewModel by lazy {
        ViewModelProvider(context as AppCompatActivity).get(ArticleViewModel::class.java)
    }

    init {
        // Corrección: Eliminado el addObserver innecesario
        viewModel.ivaPercentage.observe(context as LifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.referencia == newItem.referencia
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        with(holder.binding) {
            root.setBackgroundColor(getColorForType(article.tipus))
            tvReference.text = article.referencia
            tvDescription.text = article.descripcio
            tvPrice.text = "%.2f€".format(viewModel.calculatePriceWithIva(article.preuSenseIva))
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