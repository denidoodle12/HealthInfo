package com.expert.healthinfo.detail

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

    private var _binding: ActivityDetailBinding?= null
    private val binding get() = _binding!!

//    private lateinit var headlines: Headlines
    private var isFavorite = false
    private var detailHeadlines: Headlines? = null

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailHeadlines = getParcelableExtra(intent, EXTRA_DETAIL_DATA, Headlines::class.java)
        showDetailHeadlines(detailHeadlines)

        detailHeadlines?.let { headline ->
            headline.idHeadlines?.let { id ->
                detailViewModel.isHeadlineFavorite(id).observe(this) { isFav ->
                    isFavorite = isFav
                    setFavoriteState(isFavorite)
                }
            }
        }

        setupFavorite()

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
                    Toast.makeText(this, "Success delete to Favorite", Toast.LENGTH_SHORT).show()
                } else {
                    headline.isFavorite = true
                    detailViewModel.insertHeadlinesFavorite(headline)
                    Toast.makeText(this, "Success add to Favorite", Toast.LENGTH_SHORT).show()
                }
                isFavorite = !isFavorite
                setFavoriteState(isFavorite)
            }
        }
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite))
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_border))
        }
    }

//    private fun setFavorite(detailHeadlines: Headlines?) {
//        binding.fabFavorite.setOnClickListener {
//            if (isFavorite == true) {
//                detailHeadlines?.isFavorite = false
//                detailViewModel.insertHeadlinesFavorite(detailHeadlines!!)
//                Toast.makeText(this, "Success add to Favorite", Toast.LENGTH_SHORT).show()
//                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite_border))
//            } else {
//                detailHeadlines?.isFavorite = true
//                detailViewModel.deleteHeadlinesFavorite(detailHeadlines!!)
//                Toast.makeText(this, "Success delete to Favorite", Toast.LENGTH_SHORT).show()
//                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.favorite))
//            }
//        }
//    }

    companion object {
        const val EXTRA_DETAIL_DATA = "extra_detail_data"
    }
}