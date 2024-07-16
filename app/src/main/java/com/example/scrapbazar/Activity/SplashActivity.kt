package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.scrapbazar.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim)
        logoImageView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreference = getSharedPreferences("com.example.scrapbazar", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreference.getBoolean("isLoggedIn", false)
            val introShown = sharedPreference.getBoolean("intro_shown", false)

            when {
                !introShown -> {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                isLoggedIn -> {
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }, 3000) // Splash screen timeout duration
    }
}
