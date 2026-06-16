package com.expert.healthinfo.detail

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableExtra
import com.bumptech.glide.Glide
import com.expert.healthinfo.R
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private var isFavorite = false
    private var detailHeadlines: Headlines? = null

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailHeadlines = getParcelableExtra(intent, EXTRA_DETAIL_DATA, Headlines::class.java)
        showDetailHeadlines(detailHeadlines)

        val headlineId = detailHeadlines?.idHeadlines
        if (headlineId != null) {
            detailViewModel.isHeadlineFavorite(headlineId).observe(this) { isFav ->
                isFavorite = isFav
                setFavoriteState(isFavorite)
            }
        }

        setupFavorite()
        setupNavigation()
    }

    private fun showDetailHeadlines(detailHeadlines: Headlines?) {
        detailHeadlines.let {
            Glide.with(this@DetailActivity)
                .load(detailHeadlines?.urlToImage)
                .into(binding.ivDetailImg)

            binding.tvDetailTitle.text = detailHeadlines?.title
            binding.tvDetailAuthor.text = detailHeadlines?.author
            binding.tvDetailDescription.text = detailHeadlines?.description
        }
    }

    private fun setupFavorite() {
        binding.fabFavorite.setOnClickListener {
            detailHeadlines?.let { headline ->
                if (isFavorite) {
                    headline.isFavorite = false
                    detailViewModel.deleteHeadlinesFavorite(headline)
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    headline.isFavorite = true
                    detailViewModel.insertHeadlinesFavorite(headline)
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                isFavorite = !isFavorite
                setFavoriteState(isFavorite)
            }
        }
    }

    private fun setupNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        val context = binding.root.context
        if (isFavorite) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite))
            binding.fabFavorite.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            binding.fabFavorite.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.colorOnPrimary)
            )
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_border))
            binding.fabFavorite.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.colorTertiaryContainer)
            )
            binding.fabFavorite.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.colorTertiary)
            )
        }
    }

    /** Override finish() agar animasi slide out konsisten, termasuk saat back gesture/tombol sistem */
    @Suppress("DEPRECATION")
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_DETAIL_DATA = "extra_detail_data"
    }
}