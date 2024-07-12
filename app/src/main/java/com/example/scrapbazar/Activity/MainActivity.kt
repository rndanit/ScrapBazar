package com.example.scrapbazar.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.scrapbazar.Adapter.ImageSliderAdapter
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val intent= Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.skipText.setOnClickListener {
            val intent=Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

