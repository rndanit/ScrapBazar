package com.example.scrapbazar.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.LoginRequest
import com.example.scrapbazar.DataModel.LoginResponse
import com.example.scrapbazar.DataModel.OtpResponse
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivityOtpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {

    private lateinit var mobileNumber: String
    lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mobileNumber = intent.getStringExtra("mobile_number").toString()

        val otpEditText1: EditText = findViewById(R.id.editText1)
        val otpEditText2: EditText = findViewById(R.id.editText2)
        val otpEditText3: EditText = findViewById(R.id.editText3)
        val otpEditText4: EditText = findViewById(R.id.editText4)
        val otpEditText5: EditText = findViewById(R.id.editText5)
        val otpEditText6: EditText = findViewById(R.id.editText6)


        setOtpEditTextHandler(otpEditText2, otpEditText3)
        setOtpEditTextHandler(otpEditText3, otpEditText4)
        setOtpEditTextHandler(otpEditText4, otpEditText1)
        setOtpEditTextHandler(otpEditText1,otpEditText5)
        setOtpEditTextHandler(otpEditText5,otpEditText6)
        setOtpEditTextHandler(otpEditText6,null)

        //Continue button pe setonclick listener perform hua hai
        binding.continueButton.setOnClickListener {
            getData()
        }

    }


    //Post api ka function(Login ka data fetch ho rha hai)
    private fun getData() {
        val loginRequest = LoginRequest(
            mobile = mobileNumber,
            otp = 123456
        )

        RetrofitInstance.apiInterface.login(loginRequest).enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        Toast.makeText(this@OtpActivity, it.message, Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@OtpActivity,HomeActivity::class.java)
                        intent.putExtra("mobile_number",mobileNumber)
                        startActivity(intent)
                        Toast.makeText(this@OtpActivity,"${mobileNumber}",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OtpActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Toast.makeText(this@OtpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    //Edittext pe automatic position change hone ka function hai
    private fun setOtpEditTextHandler(currentEditText: EditText, nextEditText: EditText?) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    nextEditText?.requestFocus()
                }
            }
        })
    }
    }
