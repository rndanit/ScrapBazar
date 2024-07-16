package com.example.scrapbazar.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.example.scrapbazar.Adapter.ImageSliderAdapter
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.scrapbazar", MODE_PRIVATE)

        // Check if intro has been shown before
        if (hasIntroBeenShown()) {
            // If intro has been shown, go to LoginActivity directly
            navigateToLogin()
        } else {
            // If intro has not been shown, show intro and mark it as shown
            setContentView(binding.root)
            viewPager = findViewById(R.id.viewPager)

            val images = listOf(
                R.drawable.hot_delivery,
                R.drawable.favourite_food,
                R.drawable.p
            )

            val adapter = ImageSliderAdapter(this, images)
            viewPager.adapter = adapter

            binding.nextButton.setOnClickListener {
                navigateToLogin()
            }
            binding.skipText.setOnClickListener {
                navigateToLogin()
            }

            // Mark intro as shown
            markIntroAsShown()
        }
    }

    private fun hasIntroBeenShown(): Boolean {
        return sharedPreferences.getBoolean("intro_shown", false)
    }

    private fun markIntroAsShown() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("intro_shown", true)
        editor.apply()
    }

    private fun navigateToLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close MainActivity so it cannot be returned to
    }
}
