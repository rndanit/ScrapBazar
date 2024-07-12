package com.example.scrapbazar.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.scrapbazar.R

class AppBarActivity : AppCompatActivity() {

    lateinit var notificationBell:ImageView
    lateinit var whatsappIcon:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar)

        notificationBell=findViewById(R.id.notificationbell)
        whatsappIcon=findViewById(R.id.whatsappIcon)

        notificationBell.setOnClickListener{

            val intent=Intent(this@AppBarActivity,MyNotificationActivity::class.java)
            startActivity(intent)
        }
    }
}