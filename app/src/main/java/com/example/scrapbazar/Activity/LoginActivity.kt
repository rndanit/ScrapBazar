package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.OtpRequest
import com.example.scrapbazar.DataModel.OtpResponse
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mobilenumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                val drawable: Drawable? = if (input.length == 10) {
                    ContextCompat.getDrawable(this@LoginActivity, R.drawable.green_right)
                } else {
                    ContextCompat.getDrawable(this@LoginActivity, R.drawable.right)
                }
                binding.mobilenumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
        })

        binding.sendOtpButton.setOnClickListener {

            val MobileNumber = binding.mobilenumber.text.toString()
            if (MobileNumber.length == 10) {
                getData(MobileNumber)
            } else {
                Toast.makeText(this, "Please fill in a valid 10-digit number", Toast.LENGTH_SHORT).show()
            }

        }

        binding.skipButton.setOnClickListener {

            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)

        }
    }

    private fun getData(MobileNumber: String) {


        val request = OtpRequest(mobile =MobileNumber)

        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        RetrofitInstance.apiInterface.sendOtp(request).enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    otpResponse?.let {
                        Log.d("OTP Response", "Message: ${it.message}, No: ${it.no}, ID: ${it.id}")
                        Toast.makeText(this@LoginActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                        val editor = sharedPreference.edit()
                        editor.putInt("otp_id", it.id)
                        editor.putString("mobile_number", MobileNumber)  // Store the mobile number
                        editor.apply()

                        val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                        intent.putExtra("mobile_number", MobileNumber)
                        startActivity(intent)
                    }
                } else {
                    Log.e("OTP Response", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                Log.e("OTP Response", "Failure: ${t.message}")
            }
        })
    }

}




