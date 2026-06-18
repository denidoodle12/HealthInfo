package com.expert.healthinfo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.expert.healthinfo.R
import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.ui.HealthAdapter
import com.expert.healthinfo.about.AboutActivity
import com.expert.healthinfo.databinding.ActivityMainBinding
import com.expert.healthinfo.detail.DetailActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    /** Track apakah user sedang aktif melakukan pencarian */
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set ukuran hint text SearchView ke 14sp (queryTextSize tidak tersedia via XML)
        val searchAutoComplete = binding.searchViewItem
            .findViewById<android.widget.AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete?.textSize = 14f

        showData()
        setupSearch()
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
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.refresh()
        }

        mainViewModel.headlines.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        if (!binding.swipeRefresh.isRefreshing) {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        binding.swipeRefresh.visibility = View.VISIBLE
                        binding.viewError.visibility = View.GONE
                        binding.viewEmpty.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.viewError.visibility = View.GONE

                        if (result.data.isNullOrEmpty()) {
                            binding.swipeRefresh.visibility = View.GONE
                            binding.viewEmpty.visibility = View.VISIBLE
                            updateEmptyStateMessage()
                        } else {
                            binding.viewEmpty.visibility = View.GONE
                            binding.swipeRefresh.visibility = View.VISIBLE
                            healthAdapter.submitList(result.data)
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        binding.swipeRefresh.visibility = View.GONE
                        binding.viewEmpty.visibility = View.GONE
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

    private fun setupSearch() {
        binding.searchViewItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchViewItem.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty().trim()
                isSearching = query.isNotEmpty()
                mainViewModel.setSearchQuery(query)
                return true
            }
        })
    }

    /**
     * Update pesan empty state secara kontekstual:
     * - Saat search aktif: "No Articles Found" + "Try a different keyword"
     * - Saat tidak search: "No Articles Available" + "Pull down to refresh"
     */
    private fun updateEmptyStateMessage() {
        if (isSearching) {
            binding.tvEmptyTitle.setText(R.string.empty_search_title)
            binding.tvEmptySubtitle.setText(R.string.empty_search_subtitle)
            binding.tvEmptyIcon.text = "\uD83D\uDD0D" // 🔍
        } else {
            binding.tvEmptyTitle.setText(R.string.empty_data_title)
            binding.tvEmptySubtitle.setText(R.string.empty_data_subtitle)
            binding.tvEmptyIcon.text = "\uD83D\uDCF0" // 📰
        }
    }

    private fun setupAction() {
        binding.imgFavorite.setOnClickListener {
            installFavoriteModule()
        }
        binding.imgAbout.setOnClickListener {
            toAbout()
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
                    Toast.makeText(this, R.string.module_install_success, Toast.LENGTH_SHORT).show()
                    toFavorite()
                }
                .addOnFailureListener {
                    Toast.makeText(this, R.string.module_install_failed, Toast.LENGTH_SHORT).show()
                }
        }
    }

    @Suppress("DEPRECATION")
    private fun toFavorite() {
        try {
            startActivity(Intent(this, Class.forName("com.expert.healthinfo.favorite.FavoriteActivity")))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.module_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION")
    private fun toAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}