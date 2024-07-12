package com.example.scrapbazar.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.scrapbazar.R

class MyNotificationActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notification)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "My Notification" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    }
