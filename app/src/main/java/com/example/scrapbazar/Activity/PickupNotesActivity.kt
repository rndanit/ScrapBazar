package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.CreateQueryRequest
import com.example.scrapbazar.DataModel.CreateQueryResponse
import com.example.scrapbazar.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class PickupNotesActivity : AppCompatActivity() {

    private var isClicked = false
    private lateinit var radioButton: RadioButton
    private lateinit var textView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var editView: EditText
    private lateinit var pickUpButton: Button
    private lateinit var commentNotes: EditText
    private var rightDrawable: Drawable? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_notes)

        // Initialize UI elements
        radioButton = findViewById(R.id.radioButton)
        textView = findViewById(R.id.dateChangedTextView)
        dateTextView = findViewById(R.id.dateTextView)
        editView = findViewById(R.id.notesEdittext)
        pickUpButton = findViewById(R.id.pickUp_Btn)
        commentNotes = findViewById(R.id.notesEdittext)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Pickup Notes"
        toolbar.setTitleTextColor(getColor(R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        // Save the right drawable to restore later
        rightDrawable = ContextCompat.getDrawable(this, R.drawable.right)

        // Get current date and time
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()

        // Set the current date in dateTextView
        dateTextView.text = "Current Date: $currentDate"

        // Store current date and time in SharedPreferences
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("current_date", currentDate)
        editor.putString("current_time", currentTime)
        editor.apply()


        // Retrieve Intent extras
        val selectedProductNames = intent.getStringArrayListExtra("selectedProductNames") ?: arrayListOf()
        val firstName = intent.getStringExtra("first_name") ?: ""
        val lastName = intent.getStringExtra("last_name") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val mobileNumber = intent.getStringExtra("mobile_number") ?: ""
        val selectedVolume = intent.getStringExtra("selected_volume") ?: ""

        // Add text change listener
        editView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    editView.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
                } else {
                    editView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val checkedDrawable = ContextCompat.getDrawable(this, R.drawable.green_right)
        val uncheckedDrawable = ContextCompat.getDrawable(this, com.bumptech.glide.R.drawable.abc_btn_radio_material)

        textView.setOnClickListener {
            isClicked = !isClicked
            if (isClicked) {
                textView.setTextColor(Color.GREEN)
                radioButton.buttonDrawable = checkedDrawable
                dateTextView.text = "Estimated Date : ${getNextSunday()}"
            } else {
                textView.setTextColor(Color.BLACK)
                radioButton.buttonDrawable = uncheckedDrawable
                dateTextView.text = "Current Date : $currentDate"
            }
        }

        radioButton.setOnClickListener {
            isClicked = !isClicked
            if (isClicked) {
                textView.setTextColor(Color.GREEN)
                radioButton.buttonDrawable = checkedDrawable
                dateTextView.text = "Estimated Date : ${getNextSunday()}"
            } else {
                textView.setTextColor(Color.BLACK)
                radioButton.buttonDrawable = uncheckedDrawable
                dateTextView.text = "Current Date : $currentDate"
            }
        }

        pickUpButton.setOnClickListener {
            val otpId = sharedPreference.getInt("otp_id", -1)
            val customerName = "$firstName $lastName"
            val commentNotesText = commentNotes.text.toString()
            val dateText = dateTextView.text.toString()
            val estimatedDate = extractDate(dateText)

            Log.d("PickupNotesActivity", "otpId: $otpId")
            Log.d("PickupNotesActivity", "firstName: $firstName")
            Log.d("PickupNotesActivity", "lastName: $lastName")
            Log.d("PickupNotesActivity", "address: $address")
            Log.d("PickupNotesActivity", "customerName: $customerName")
            Log.d("PickupNotesActivity", "mobileNumber: $mobileNumber")
            Log.d("PickupNotesActivity", "commentNotes: $commentNotesText")
            Log.d("PickupNotesActivity", "selectedVolume: $selectedVolume")
            Log.d("PickupNotesActivity", "productName: $selectedProductNames")

            if (otpId != -1) {
                postData(otpId, customerName, mobileNumber, address, commentNotesText, selectedVolume, estimatedDate, selectedProductNames)
                val intent = Intent(this@PickupNotesActivity, ThankyouActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to retrieve the Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun extractDate(dateText: String): String {
        return dateText.substringAfter("Estimated Date : ").trim()
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun getNextSunday(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, (Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK) + 7) % 7)
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun postData(
        otpId: Int,
        customerName: String,
        mobileNumber: String,
        address: String,
        commentNotes: String,
        selectedVolume: String,
        estimatedDate: String,
        selectedProductNames: ArrayList<String>?
    ) {
        val request = CreateQueryRequest(
            c_id = otpId,
            customer_name = customerName,
            mobile = mobileNumber,
            product_name = selectedProductNames,
            product_volume = selectedVolume,
            Address = address,
            next_sunday = estimatedDate,
            comment = commentNotes
        )
        Log.d("PickupNotesActivity", "CreateQueryRequest: $request")

        RetrofitInstance.apiInterface.createQuery(request).enqueue(object : Callback<CreateQueryResponse?> {
            override fun onResponse(
                call: Call<CreateQueryResponse?>,
                response: Response<CreateQueryResponse?>
            ) {
                if (response.isSuccessful) {
                    val queryResponse = response.body()
                    queryResponse?.let {
                        val primaryId = it.inquiry.primary_id
                        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putInt("primary_id", primaryId)
                        editor.apply()
                        Toast.makeText(this@PickupNotesActivity, "Query saved successfully", Toast.LENGTH_SHORT).show()
                        Log.d("PickupNotesActivity", "Query saved successfully: $queryResponse")
                    }
                } else {
                    Log.e("PickupNotesActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CreateQueryResponse?>, t: Throwable) {
                Log.e("PickupNotesActivity", "Failure: ${t.message}")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
