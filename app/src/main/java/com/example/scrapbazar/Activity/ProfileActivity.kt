package com.example.scrapbazar.Activity

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatDelegate
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.GetProfileDataResponse
import com.example.scrapbazar.DataModel.UserRequest
import com.example.scrapbazar.R
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var EditButton: Button
    private lateinit var ManageAddress: MaterialCardView
    private lateinit var logoutAccount: MaterialCardView
    private lateinit var profileUserdata: TextView
    private lateinit var profileNumber: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImage: ImageView

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_profile)

        // Find the Value of Variables
        profileEmail = findViewById(R.id.profileEmail)
        profileImage = findViewById(R.id.mainProfileImage)
        profileNumber = findViewById(R.id.profileNumber)
        profileUserdata = findViewById(R.id.profileUserName)

        //Find the Id of a Toolbar.
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Profile" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        // Get data function call
        getProfileData()

        // Find the id of Edit Button
        EditButton = findViewById(R.id.editProfileButton)

        EditButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ManageProfileDataActivity::class.java)
            startActivity(intent)
        }

        // Find the id of manage Address Cardview
        ManageAddress = findViewById(R.id.manageAddress)
        ManageAddress.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ManageAddressActivity::class.java)
            startActivity(intent)
        }

        //LogOut Functionality.
        logoutAccount = findViewById(R.id.logoutAccount)
        logoutAccount.setOnClickListener {
            // Clear SharedPreferences data
            val sharedPreference = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.clear()
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    //getProfileData Function.
    private fun getProfileData() {
        // Get the stored ID
        val sharedPreference = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        val mobileNumber = sharedPreference.getString("mobile_number", "")

        // Set the mobile number to profileNumber TextView
        profileNumber.text = mobileNumber

        RetrofitInstance.apiInterface.getProfileData(otpId).enqueue(object : Callback<GetProfileDataResponse?> {
            override fun onResponse(
                call: Call<GetProfileDataResponse?>,
                response: Response<GetProfileDataResponse?>
            ) {
                val responsedata = response.body()
                if (responsedata != null) {
                    profileUserdata.text = responsedata.first_name
                    profileEmail.text = responsedata.email
                    Picasso.get().load(responsedata.image).into(profileImage)
                    Log.d(" Profile Response", "onResponse: $responsedata")
                    Toast.makeText(this@ProfileActivity, "Response Profile Data:$responsedata", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@ProfileActivity, "Response Profile Data:$response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetProfileDataResponse?>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d("Update Data", "onFailure: ${t.message}")
            }
        })
    }

    override fun onResume() {
        super.onResume()

        // Get data function call
        getProfileData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
