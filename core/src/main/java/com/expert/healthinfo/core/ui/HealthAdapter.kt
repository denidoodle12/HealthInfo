package com.expert.healthinfo.core.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.expert.healthinfo.core.R
import com.expert.healthinfo.core.databinding.ItemsArticleBinding
import com.expert.healthinfo.core.domain.model.Headlines

class HealthAdapter : ListAdapter<Headlines, HealthAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Headlines) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val headlines = getItem(position)
        holder.bind(headlines)
    }

    inner class ListViewHolder(private val binding: ItemsArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(headlines: Headlines) {
            Glide.with(binding.ivMediaCover.context)
                .load(headlines.urlToImage)
                .into(binding.ivMediaCover)

            binding.tvAuthor.text = headlines.author
            binding.tvTitleName.text = headlines.title
            binding.tvDescHeadlines.text = headlines.description

            val context = binding.root.context
            if (headlines.isFavorite == true) {
                binding.ivBookmark.setImageResource(R.drawable.baseline_bookmark_24)
                binding.ivBookmark.setBackgroundResource(R.drawable.s_circle_shape_lightgreen)
                binding.ivBookmark.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.colorOnPrimary)
                )
            } else {
                binding.ivBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)
                binding.ivBookmark.setBackgroundResource(R.drawable.s_circle_shape_bookmarks_yellow)
                binding.ivBookmark.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.colorTertiary)
                )
            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
            // Bookmark adalah indikator visual saja — action add/remove hanya dari halaman detail
            binding.ivBookmark.isClickable = false
            binding.ivBookmark.isFocusable = false
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Headlines> =
            object : DiffUtil.ItemCallback<Headlines>() {
                override fun areItemsTheSame(oldItem: Headlines, newItem: Headlines): Boolean {
                    return oldItem.idHeadlines == newItem.idHeadlines
                }

                override fun areContentsTheSame(oldItem: Headlines, newItem: Headlines): Boolean {
                    return oldItem == newItem
                }
            }
    }
}