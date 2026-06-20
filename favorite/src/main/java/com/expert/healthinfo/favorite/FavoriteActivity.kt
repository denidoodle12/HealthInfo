package com.expert.healthinfo.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.expert.healthinfo.R
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

        setupNavigation()
        showData()
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
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

        // Tampilkan loading saat awal
        binding.progressBar.visibility = View.VISIBLE
        binding.viewEmpty.visibility = View.GONE
        binding.rvFavorite.visibility = View.GONE

        favoriteViewModel.headlines.observe(this) { favoriteList ->
            // Sembunyikan loading setelah data tiba (Room sangat cepat)
            binding.progressBar.visibility = View.GONE

            if (favoriteList.isNullOrEmpty()) {
                // Tampilkan empty state
                binding.rvFavorite.visibility = View.GONE
                binding.viewEmpty.visibility = View.VISIBLE
            } else {
                // Tampilkan daftar favorit
                binding.viewEmpty.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
                healthAdapter.submitList(favoriteList)
            }
        }
    }

    /** Override finish() agar animasi fade konsisten saat kembali dari FavoriteActivity */
    @Suppress("DEPRECATION")
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onDestroy() {
        // Detach adapter from RecyclerView sebelum nulling binding
        // untuk memutus reference chain: RecyclerView → Adapter → lambda → Activity context
        binding.rvFavorite.adapter = null
        super.onDestroy()
        _binding = null
    }
}
