package com.expert.healthinfo.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expert.healthinfo.R
import com.expert.healthinfo.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private var _binding: ActivityAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
