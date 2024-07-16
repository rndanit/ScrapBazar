package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Adapter.AddressAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.UserRequest
import com.example.scrapbazar.DataModel.UserResponseSubListItem
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivitySelectPickupAddressBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectPickupAddressActivity : AppCompatActivity() {

    private lateinit var mobileNumber: String
    private lateinit var binding: ActivitySelectPickupAddressBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private var selectedItem: UserResponseSubListItem? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPickupAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Mobile number data get
        mobileNumber = intent.getStringExtra("mobile_number").toString()

        //Product name data get
        val selectedProductNames = intent.getStringArrayListExtra("selectedProductNames")

        //Volume text data get
        val selectedVolume = intent.getStringExtra("selected_volume")

        // Set button to default disabled state
        binding.continueAddressBtn.isEnabled = false
        // binding.continueAddressBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Select pickup address" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        // Show progress bar and text
        binding.progressTextview.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE

        // Initialize RecyclerView
        recyclerView = binding.addressRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)


        binding.addressContainer.setOnClickListener {
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivity(intent)
        }

        val sharedPreference = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        if (otpId != -1) {

            fetchAddresses(otpId)
        } else {
            Toast.makeText(this, "Failed to retrieve OTP ID", Toast.LENGTH_SHORT).show()
        }
        binding.continueAddressBtn.setOnClickListener {
            Log.d("SelectPickup", "Continue button clicked")
            selectedItem?.let {
                Log.d("SelectPickup", "Selected Item: $it")
                val intent = Intent(this@SelectPickupAddressActivity, PickupNotesActivity::class.java).apply {
                    putExtra("first_name", it.first_name)
                    putExtra("last_name", it.last_name)
                    putExtra("address", it.address)
                    putExtra("selected_volume", selectedVolume)
                    putExtra("mobile_number",mobileNumber)
                    putStringArrayListExtra("selectedProductNames", selectedProductNames)
                }
                Toast.makeText(this, "Navigating to PickupNotesActivity", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "No address selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAddresses(otpId: Int) {
        val request = UserRequest(id = otpId)
        Toast.makeText(this, "id:$request", Toast.LENGTH_SHORT).show()
        RetrofitInstance.apiInterface.fetchAddresses(request).enqueue(object : Callback<List<List<UserResponseSubListItem>>?> {
            override fun onResponse(
                call: Call<List<List<UserResponseSubListItem>>?>,
                response: Response<List<List<UserResponseSubListItem>>?>
            ) {
                Toast.makeText(this@SelectPickupAddressActivity, "Success: ${response.code()}", Toast.LENGTH_SHORT).show()
                Log.d("DataFetchApi", "onResponse: $response")
                val userData: List<UserResponseSubListItem> = response.body()!!.get(0)
                addressAdapter = AddressAdapter(applicationContext, userData) { item ->
                    selectedItem = item
                    binding.continueAddressBtn.isEnabled = true
                    binding.continueAddressBtn.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green)
                    Log.d("SelectPickup", "Item selected: $item")
                }
                recyclerView.adapter = addressAdapter

                binding.progressTextview.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<List<UserResponseSubListItem>>?>, t: Throwable) {
                Log.e("Fetch Address Response", "Failure: ${t.message}")

                binding.progressTextview.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
