package com.example.scrapbazar.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.UpdateProfileResponse
import com.example.scrapbazar.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class ManageProfileDataActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var saveButton: Button
    private val REQUEST_IMAGE_PICK = 1
    private var selectedImageUri: Uri? = null
    private lateinit var mProgress: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_profile_data)

        //Find the Id of a Variables.
        profileImageView=findViewById(R.id.profileImageView)
        saveButton=findViewById(R.id.saveButton)
        userName=findViewById(R.id.userName)
        email=findViewById(R.id.email)
        val btnPickImage = findViewById<FloatingActionButton>(R.id.cameraButton)

        //Find the Id of a Toolbar.
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Manage Profile" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))


        //Functionality of a ProgressBar.
        mProgress = ProgressDialog(this).apply {
            setTitle("Loading Data....")
            setMessage("Please wait...")
            setCancelable(false)
            setIndeterminate(true)
        }

        //Camera Button setOnClickListener to open the Image Chooser.
        btnPickImage.setOnClickListener {
            openImagePicker()
        }

        //Save Button setOnClickListener to Upload the Data to the server.
        saveButton.setOnClickListener {
            selectedImageUri?.let {
                mProgress.show()
                uploadImage(selectedImageUri!!)
                finish()
            } ?: run {
                Toast.makeText(this, "Please pick an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //openImagePicker function.
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    // onActivityResult Function.
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            profileImageView.setImageURI(selectedImageUri)
        }
    }

    //Upload Image Function.
    private fun uploadImage(imageUri: Uri) {

        // Retrieve the ID from SharedPreferences
        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        val name = userName.text.toString().trim()
        val email = email.text.toString().trim()
        val id = otpId


        val file = File( getRealPathFromURI(imageUri))
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val requestId = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id.toString())
        val requestName = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
        val requestEmail = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)


        RetrofitInstance.apiInterface.submitForm(requestId, requestName, requestEmail, body).enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                if (response.isSuccessful) {
                    val contentType = response.headers()["Content-Type"]
                    contentType?.let {
                        Log.e("API Response", "Content-Type: $it")
                    } ?: run {
                        Log.e("API Response", "Content-Type header is not present.")
                    }

                    val updateResponse = response.body()
                    val message = updateResponse?.message
                    mProgress.dismiss()
                    Toast.makeText(this@ManageProfileDataActivity, message, Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e("path", "Error response: $errorBody")
                        Toast.makeText(this@ManageProfileDataActivity, "not success $errorBody", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                mProgress.dismiss()
                Toast.makeText(this@ManageProfileDataActivity, "Code: ", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@ManageProfileDataActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("path", t.localizedMessage ?: "")
            }
        })
    }

    //getRealPathFromURI Function.
    private fun getRealPathFromURI(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = contentResolver.query(contentUri, projection, null, null, null) ?: return ""
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }

    //OnBackPressed Function.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

