package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.cancelInquiryRequest
import com.example.scrapbazar.DataModel.cancelInquiryResponse
import com.example.scrapbazar.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CancelInqueryActivity : AppCompatActivity() {
    private lateinit var cancelButton:Button
    private var selectedReason: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_inquery)

        //find the id of button
        cancelButton=findViewById(R.id.cancelRequestButton)

        //find the id of spinner
        val spinner: Spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_items,
            android.R.layout.simple_spinner_dropdown_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

        // Set an item selected listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedReason = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    this@CancelInqueryActivity,
                    "Selected: $selectedReason",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


        val reason=selectedReason.toString()

        // Set up the button click listener
        cancelButton.setOnClickListener {
            getData(reason)
            finish()
        }


    }

    private fun getData(reason: String) {

        val primaryId = intent.getStringExtra("REQUEST_ID")
        Toast.makeText(this@CancelInqueryActivity, "Cancel Primary Id:-$primaryId", Toast.LENGTH_SHORT).show()

        val request=cancelInquiryRequest(reason=reason, primary_id = primaryId.toString())

        RetrofitInstance.apiInterface.cancelDataPass(request).enqueue(object : Callback<cancelInquiryResponse?> {
            override fun onResponse(
                call: Call<cancelInquiryResponse?>,
                response: Response<cancelInquiryResponse?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CancelInqueryActivity, "Cancel request sent successfully", Toast.LENGTH_SHORT).show()
                    Log.d("Cancel Response", "onResponse: $response")
                } else {
                    Toast.makeText(this@CancelInqueryActivity, "Failed to send cancel request", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<cancelInquiryResponse?>, t: Throwable) {
                Toast.makeText(this@CancelInqueryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}
