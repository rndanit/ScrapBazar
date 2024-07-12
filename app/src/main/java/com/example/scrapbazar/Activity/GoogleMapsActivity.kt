package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Adapter.AddressAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.AddressRequest
import com.example.scrapbazar.DataModel.AddressResponse
import com.example.scrapbazar.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.scrapbazar.databinding.ActivityGoogleMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {


    //Declaration of a Variables
    private lateinit var edFirstname: EditText
    private lateinit var edLastname: EditText
    private lateinit var edPincode: EditText
    private lateinit var edState: EditText
    private lateinit var edCity: EditText
    private lateinit var edAddress: EditText
    private lateinit var edLandmark: EditText
    private lateinit var edArea: EditText
    private lateinit var MapButton: Button
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Find the variable
        edFirstname = findViewById(R.id.et_firstname)
        edLastname = findViewById(R.id.et_lastname)
        edPincode = findViewById(R.id.et_pincode)
        edState = findViewById(R.id.et_state)
        edCity = findViewById(R.id.et_city)
        edAddress = findViewById(R.id.et_address)
        edLandmark = findViewById(R.id.et_landmark)
        edArea = findViewById(R.id.et_area)
        MapButton = findViewById(R.id.mapbutton)

        //Functionality of MapButton
        MapButton.isEnabled = false
        edFirstname.addTextChangedListener(loginWatcher)
        edLastname.addTextChangedListener(loginWatcher)
        edPincode.addTextChangedListener(loginWatcher)
        edState.addTextChangedListener(loginWatcher)
        edCity.addTextChangedListener(loginWatcher)
        edAddress.addTextChangedListener(loginWatcher)
        edLandmark.addTextChangedListener(loginWatcher)
        edArea.addTextChangedListener(loginWatcher)


        //Add a setOnClickListener on a mapButton
        MapButton.setOnClickListener {
            // Retrieve the ID from SharedPreferences
            val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val otpId = sharedPreference.getInt("otp_id", -1)

            if (otpId != -1) {
                postData(otpId)
                
                val intent = Intent(this@GoogleMapsActivity, SelectPickupAddressActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to retrieve OTP ID", Toast.LENGTH_SHORT).show()
            }

        }

        //Functionality of a right drawable colour change
        val editTexts = listOf(
            edFirstname,
            edLastname,
            edPincode,
            edState,
            edCity,
            edAddress,
            edLandmark,
            edArea
        )
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {


                    val input = s.toString()
                    val drawable: Drawable? = if (input.length > 0) {
                        ContextCompat.getDrawable(this@GoogleMapsActivity, R.drawable.green_right)
                    } else {
                        ContextCompat.getDrawable(this@GoogleMapsActivity, R.drawable.right)
                    }
                    editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                }
            })
        }
    }

    //Functionality of postData Function to the Post API(Yeh Data ko Post kar rhe hai API se).
    private fun postData(otpId: Int) {

        val first_name = binding.etFirstname.text.toString()
        val last_name = binding.etLastname.text.toString()
        val pin_code = binding.etPincode.text.toString()
        val state = binding.etState.text.toString()
        val city = binding.etCity.text.toString()
        val address = binding.etAddress.text.toString()
        val landmark = binding.etLandmark.text.toString()
        val area = binding.etArea.text.toString()
        val lat = "70.11"
        val long = "33.11"


        val request = AddressRequest(
            c_id = otpId,
            first_name = first_name,
            last_name = last_name,
            pincode = pin_code,
            state = state,
            city = city,
            address = address,
            area = area,
            landmark = landmark,
            lat = lat,
            long = long
        )
        Log.d("Response data", "getData: ${request}")

        RetrofitInstance.apiInterface.address(request).enqueue(object : Callback<AddressResponse?> {
            override fun onResponse(
                call: Call<AddressResponse?>,
                response: Response<AddressResponse?>
            ) {
                if (response.isSuccessful) {
                    val AddressResponse = response.body()
                    AddressResponse?.let {
                        Toast.makeText(
                            this@GoogleMapsActivity,
                            "Address saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Post Map Address", "onResponse:${AddressResponse} ")

                    }
                } else {
                    Log.e("Post Address Response", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AddressResponse?>, t: Throwable) {
                Log.e("Post Address Response", "Failure: ${t.message}")
            }
        })
    }


    private val loginWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence, start: Int, count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence, start: Int, before: Int, count: Int
        ) {
            val FirstName = edFirstname.text.toString()
            val LastName = edLastname.text.toString()
            val Pincode = edPincode.text.toString()
            val State = edState.text.toString()
            val City = edCity.text.toString()
            MapButton.isEnabled =
                FirstName.isNotEmpty() && LastName.isNotEmpty() && Pincode.isNotEmpty() && State.isNotEmpty() && City.isNotEmpty()


        }

        override fun afterTextChanged(editable: Editable) {}
    }

    //Functionality of a Map(Yeh map ko activity me add)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(70.11, 33.11)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in RndTechNoSoft"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

}
