package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.scrapbazar.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim)
        logoImageView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreference.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000) // Splash screen timeout duration
    }
}
