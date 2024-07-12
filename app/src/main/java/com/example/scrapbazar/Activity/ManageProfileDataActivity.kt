package com.example.scrapbazar.Activity

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.UpdateProfileResponse
import com.example.scrapbazar.R
import com.example.scrapbazar.Util.FileUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ManageProfileDataActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var profileImageView: ImageView
    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var saveButton: Button

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_profile_data)

        // Find the views
        userName = findViewById(R.id.userName)
        email = findViewById(R.id.email)
        profileImageView = findViewById(R.id.profileImageView)
        saveButton = findViewById(R.id.saveButton)

        // Get the stored ID
        val sharedPreference = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Manage Profile"
        toolbar.setTitleTextColor(getColor(R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        val cameraButton: FloatingActionButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            if (otpId != -1) {
                postUserData(otpId)
                finish()
            } else {
                Toast.makeText(this, "Invalid OTP ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postUserData(otpId: Int) {
        val userNameValue = userName.text.toString().trim()
        val emailValue = email.text.toString().trim()

        if (userNameValue.isEmpty() || emailValue.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            return
        }


        // Prepare the form data
        val idPart = RequestBody.create("text/plain".toMediaTypeOrNull(), otpId.toString())
        val userNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), userNameValue)
        val emailPart = RequestBody.create("text/plain".toMediaTypeOrNull(), emailValue)
//
//        val imagePart: MultipartBody.Part? = selectedImageUri?.let { uri ->
//            val file = createFileFromUri(uri)
//            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//            MultipartBody.Part.createFormData("image", file.name, requestFile)
//        }

        //Convert Uri to File using FileUtil
        val imageFile = FileUtil.getFileFromUri(this, selectedImageUri!!)
        if (imageFile == null) {
            Toast.makeText(this, "Failed to get image file", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val imagePart =
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)


        // Logging the request parts for debugging
        Log.d("Request Data", "ID: $otpId, UserName: $userNameValue, Email: $emailValue, Image: ${imagePart?.body?.contentType()}")

        // Make the API call
        RetrofitInstance.apiInterface.submitForm(idPart, userNamePart, emailPart, imagePart).enqueue(object :
            Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Update Profile Response", "onResponse: $responseBody")
                    Toast.makeText(this@ManageProfileDataActivity, responseBody?.message ?: "Success", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@ManageProfileDataActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.d("Update Data", "onResponse: ${response.code()} - $errorBody")
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Toast.makeText(this@ManageProfileDataActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d("Update Data", "onFailure: ${t.message}")
            }
        })
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            profileImageView.setImageURI(it)
        }
    }

    private fun openGallery() {
        pickImage.launch("image/*")
    }

    private fun createFileFromUri(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(cacheDir, contentResolver.getFileName(uri))
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = this.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex("_display_name")
                if (columnIndex != -1) {
                    name = it.getString(columnIndex)
                }
            }
        }
        return name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
