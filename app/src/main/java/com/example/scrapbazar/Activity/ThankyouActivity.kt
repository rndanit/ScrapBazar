package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.scrapbazar.R

class ThankyouActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var requestId:TextView
    private lateinit var bookButton:Button
    private val handler = Handler(Looper.getMainLooper())
    private val imageResources = arrayOf(
        R.drawable.congratulation,
        R.drawable.congrats  // replace with your drawable resources
    )
    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thankyou)

        //Book More Button Functionality
        bookButton=findViewById(R.id.bookMore_Button)

        bookButton.setOnClickListener {
            val intent=Intent(this@ThankyouActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Find the id of Request ID.
        requestId=findViewById(R.id.request_id)

        //Store the value of request Id to the SharePreferences.
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val request_id = sharedPreference.getInt("primary_id", -1)

        requestId.text="Your Request id: #${request_id}"

        Toast.makeText(this@ThankyouActivity,"Request ID:${request_id}",Toast.LENGTH_SHORT).show()
        Log.d("PickupNotesActivity", "requestId: $request_id")



        //Toolbar Functionality
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.green))  // Set the toolbar color to green

        imageView = findViewById(R.id.imageView8)
        startImageSwitcher()


    }

    private fun startImageSwitcher() {
        handler.post(object : Runnable {
            override fun run() {
                imageView.setImageResource(imageResources[currentImageIndex])
                currentImageIndex = (currentImageIndex + 1) % imageResources.size
                handler.postDelayed(this, 1000)  // Change image every second
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)  // Remove callbacks to prevent memory leaks
    }
}
