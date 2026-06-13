package com.expert.healthinfo.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.expert.healthinfo.core.ui.HealthAdapter
import com.expert.healthinfo.detail.DetailActivity
import com.expert.healthinfo.favorite.databinding.ActivityFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadKoinModules(favoriteModule)

        showData()
    }

    private fun showData() {
        val healthAdapter = HealthAdapter()
        healthAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_DATA, selectedData)
            startActivity(intent)
        }

        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = healthAdapter
        }

        favoriteViewModel.headlines.observe(this) { favoriteList ->
            healthAdapter.submitList(favoriteList)
        }
    }
}
