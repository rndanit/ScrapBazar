package com.example.scrapbazar.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.RescheduleRequest
import com.example.scrapbazar.DataModel.RescheduleResponse
import com.example.scrapbazar.R
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReschedulePickupActivity : AppCompatActivity() {

    private lateinit var cardViewDatePicker: MaterialCardView
    private lateinit var tvSelectedDate: TextView
    private lateinit var rescheduleDate: TextView
    private lateinit var selectedDate: String
    private lateinit var spinnerArrow:ImageView
    private lateinit var rescheduleButton:Button

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reschedule_pickup)

        val requestId = intent.getStringExtra("REQUEST_ID")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Reschedule Pickup" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        // Initialize views
        cardViewDatePicker = findViewById(R.id.cardViewDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        rescheduleDate = findViewById(R.id.rescheduleDate)
        spinnerArrow=findViewById(R.id.spinnerArrow)
        rescheduleButton=findViewById(R.id.reschedule_Btn)

        // Set onClickListener for cardView to open DatePickerDialog
        spinnerArrow.setOnClickListener {
            showDatePicker()
        }

        //Set onclickListener for reschedule Button.
        rescheduleButton.setOnClickListener {
            reschedulePickup()
           // finish()
        }
    }

    private fun reschedulePickup() {
        val primaryId = intent.getStringExtra("REQUEST_ID")
        val rescheduleDate=rescheduleDate.text.toString()
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("rescheduleDate", rescheduleDate)
        editor.apply()
        val reschedule="Yes"
        val request=RescheduleRequest(date = rescheduleDate, primary_id = primaryId.toString(), reschedule = reschedule)

        Log.d("Reschedule Pickup Data", "getData: ${request}")

        RetrofitInstance.apiInterface.reschedule(request).enqueue(object : Callback<RescheduleResponse?> {
            override fun onResponse(
                call: Call<RescheduleResponse?>,
                response: Response<RescheduleResponse?>
            ) {
                if (response.isSuccessful) {
                    val queryResponse = response.body()
                    queryResponse?.let {
                        val message=queryResponse.message
                        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putString("Message", message)
                        editor.apply()
                        Toast.makeText(this@ReschedulePickupActivity, "Reschedule Pickup successfully", Toast.LENGTH_SHORT).show()
                        Log.d("ReschedulePickup", "Reschedule Pickup successfully: $queryResponse")
                        finish()
                    }
                } else {
                    Log.e("ReschedulePickup", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RescheduleResponse?>, t: Throwable) {
                Log.e("ReschedulePickup", "Failure: ${t.message}")
            }
        })
    }

    // Function to show DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Update selected date with the desired format
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)
                updateDateTextView(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // Function to update the TextView with selected date
    private fun updateDateTextView(date: String) {
        rescheduleDate.text = date
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}