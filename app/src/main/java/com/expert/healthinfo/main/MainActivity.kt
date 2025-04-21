package com.expert.healthinfo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.ui.HealthAdapter
import com.expert.healthinfo.databinding.ActivityMainBinding
import com.expert.healthinfo.detail.DetailActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private var _binding: ActivityMainBinding?= null
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
        healthAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_DATA, selectedData)
            startActivity(intent)
        }

        mainViewModel.headlines.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is com.expert.healthinfo.core.data.Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is com.expert.healthinfo.core.data.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        healthAdapter.submitList(result.data)
                    }
                    is com.expert.healthinfo.core.data.Result.Error -> {
                        Toast.makeText(
                            this,
                            "unable to get data ${result.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        with(binding.rvHealth) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = healthAdapter
        }
    }

    private fun setupAction() {
        binding.imgFavorite.setOnClickListener {
            toFavorite()
            installFavoriteModule()
        }
    }


    private fun installFavoriteModule(){
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if(splitInstallManager.installedModules.contains(moduleFavorite)) {
            toFavorite()
            Toast.makeText(this, "Open module", Toast.LENGTH_SHORT).show()
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
        } catch (e: Exception){
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
        }
    }

}