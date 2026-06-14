package com.expert.healthinfo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.expert.healthinfo.R
import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.ui.HealthAdapter
import com.expert.healthinfo.databinding.ActivityMainBinding
import com.expert.healthinfo.detail.DetailActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showData()
        setupAction()
    }

    private fun showData() {
        val healthAdapter = HealthAdapter()

        with(binding.rvHealth) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = healthAdapter
        }

        healthAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_DATA, selectedData)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Warna indicator swipe-to-refresh sesuai tema aplikasi
        binding.swipeRefresh.setColorSchemeResources(R.color.darkGreen)

        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.refresh()
        }

        mainViewModel.headlines.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        // Tampilkan swipe spinner atau ProgressBar (saat pertama load)
                        if (!binding.swipeRefresh.isRefreshing) {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        binding.swipeRefresh.visibility = View.VISIBLE
                        binding.viewError.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.swipeRefresh.visibility = View.VISIBLE
                        binding.viewError.visibility = View.GONE
                        healthAdapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.swipeRefresh.visibility = View.GONE
                        binding.viewError.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            binding.viewError.visibility = View.GONE
            mainViewModel.refresh()
        }
    }

    private fun setupAction() {
        binding.imgFavorite.setOnClickListener {
            installFavoriteModule()
        }
    }

    private fun installFavoriteModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            toFavorite()
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleFavorite)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(this, "Success installing module", Toast.LENGTH_SHORT).show()
                    toFavorite()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error installing module", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun toFavorite() {
        try {
            startActivity(Intent(this, Class.forName("com.expert.healthinfo.favorite.FavoriteActivity")))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
        }
    }
}