package com.example.scrapbazar.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.scrapbazar.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PickupStatusActivity : AppCompatActivity() {
    private lateinit var cancelButton: Button
    private lateinit var pickupText: TextView
    private lateinit var statusText: TextView
    private lateinit var rescheduleButton:TextView
    private lateinit var drawableFirst:ImageView
    private lateinit var drawableSecond:ImageView
    private lateinit var drawableThird:ImageView
    private lateinit var drawableFour:ImageView
    private lateinit var secondTextView: TextView
    private lateinit var thirdTextView: TextView
    private lateinit var fourthTextView: TextView
    private lateinit var fifthTextView: TextView
    private lateinit var changeOne:ImageView
    private lateinit var changeSecond:ImageView
    private lateinit var dateText:TextView
    private lateinit var date:TextView

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_status)

        // Get the data of Variables from intent.
        val requestId = intent.getStringExtra("REQUEST_ID")
        val requestDate = intent.getStringExtra("REQUEST_DATE")
        val colorCode = intent.getStringExtra("COLOR_CODE")
        val status = intent.getStringExtra("Request_Status")

        // Find by Id of Variables.
        cancelButton = findViewById(R.id.cancelId)
        pickupText = findViewById(R.id.placedStatusId)
        statusText = findViewById(R.id.statusId)
        rescheduleButton=findViewById(R.id.rescheduleId)
        drawableFirst=findViewById(R.id.drawableFirst)
        drawableSecond=findViewById(R.id.drawableSecond)
        drawableThird=findViewById(R.id.drawableThird)
        drawableFour=findViewById(R.id.drawableFourth)
        secondTextView=findViewById(R.id.secondTextview)
        thirdTextView=findViewById(R.id.thirdTextview)
        fourthTextView=findViewById(R.id.fourthTextview)
        fifthTextView=findViewById(R.id.fifthTextview)
        changeOne=findViewById(R.id.changeOne)
        changeSecond=findViewById(R.id.changeSecond)
        dateText=findViewById(R.id.dateTextId)
        date=findViewById(R.id.dateId)


        // Retrieve values from SharedPreferences
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentDate = sharedPreference.getString("current_date", "No Date Found")
        val currentTime = sharedPreference.getString("current_time", "No Time Found")

        pickupText.text = "Your Pickup Request has been placed on \n$currentDate $currentTime"
        statusText.text = "Status($status)"



        val requestIdTextView: TextView = findViewById(R.id.requestActivityId)
       // val requestDateTextView: TextView = findViewById(R.id.dateId)

        requestIdTextView.text = "RequestActivity ID : $requestId"
        date.text = " $requestDate"

        Toast.makeText(this@PickupStatusActivity, "Request Id:$requestId", Toast.LENGTH_SHORT).show()
        Toast.makeText(this@PickupStatusActivity, "Request Date:$requestDate", Toast.LENGTH_SHORT).show()

        // Cancel button functionality
        cancelButton.setOnClickListener {
            val intent = Intent(this@PickupStatusActivity, CancelInqueryActivity::class.java)
            intent.putExtra("REQUEST_ID", requestId)
            intent.putExtra("COLOR_CODE", colorCode)
            startActivity(intent)
        }
        //Reschedule button Functionality.
        // Reschedule button functionality
        rescheduleButton.setOnClickListener {
            showRescheduleConfirmationDialog()
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Pickup Request Status" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))


    }

    @SuppressLint("NewApi")
    private fun showRescheduleConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reschedule Request")
        builder.setMessage("Are you sure you want to Reschedule Pickup")
        builder.setPositiveButton("YES") { dialog, which ->
            // Handle YES button click (if needed)
            val requestId = intent.getStringExtra("REQUEST_ID")
            Toast.makeText(this, "Reschedule clicked", Toast.LENGTH_SHORT).show()

            // Add your logic for rescheduling here
            val intent=Intent(this@PickupStatusActivity,ReschedulePickupActivity::class.java)
            intent.putExtra("REQUEST_ID", requestId)
            startActivity(intent)
        }
        builder.setNegativeButton("NO") { dialog, which ->
            // Handle NO button click (if needed)
            Toast.makeText(this, "Cancel reschedule", Toast.LENGTH_SHORT).show()

        }
        val dialog = builder.create()
        dialog.show()

        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(getColor(R.color.green))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(getColor(R.color.green))



    }

    override fun onResume() {
        super.onResume()

        // Check if the activity was navigated from CancelInqueryActivity

        val status = intent.getStringExtra("Request_Status")

        if (status =="Cancelled") {
            // Hide the required elements
            rescheduleButton.visibility = View.GONE
            cancelButton.visibility=View.GONE
            drawableFirst.visibility = View.GONE
            drawableSecond.visibility = View.GONE
            drawableThird.visibility = View.GONE
            drawableFour.visibility = View.GONE
            secondTextView.visibility = View.GONE
            thirdTextView.visibility = View.GONE
            fourthTextView.visibility = View.GONE
            fifthTextView.visibility = View.GONE

            //Change the images of changeOne and changeSecond
            changeOne.setImageResource(R.drawable.full_green_right) // Replace with your new image resource
            changeSecond.setImageResource(R.drawable.full_green_right) // Replace with your new image resource

            //Change the Text of a Estimated date.
            dateText.text="Pickup Cancelled on"

        }

        /*
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val rescheduledate=sharedPreference.getString("rescheduleDate",null)
        if(rescheduledate!=null){
            date.text=rescheduledate
            rescheduleButton.visibility=View.GONE
            // Increase the width and height of the button by 50%

            drawableFirst.setImageResource(R.drawable.full_green_right)
        }

         */


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

