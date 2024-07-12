package com.example.scrapbazar.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Adapter.AddressAdapter
import com.example.scrapbazar.Adapter.UserAddressAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.DeleteAddressRequest
import com.example.scrapbazar.DataModel.DeleteAddressResponse
import com.example.scrapbazar.DataModel.UserAddressRequest
import com.example.scrapbazar.DataModel.UserAddressResponseSubListItem
import com.example.scrapbazar.DataModel.UserRequest
import com.example.scrapbazar.DataModel.UserResponseSubListItem
import com.example.scrapbazar.R
import com.example.scrapbazar.databinding.ActivitySelectPickupAddressBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageAddressActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var UserAdapter: UserAddressAdapter
    private lateinit var manageAddress:ImageButton
    private var addressList: MutableList<UserAddressResponseSubListItem> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_address)

        //Find the value
        manageAddress=findViewById(R.id.ManageAddressContainer)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Manage Address" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        recyclerView = findViewById(R.id.userAddressRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        manageAddress.setOnClickListener {

            val intent= Intent(this@ManageAddressActivity,ManageGoogleActivity::class.java)
            startActivity(intent)
        }


        val sharedPreference = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        fetchAddresses(otpId)
    }

    private fun fetchAddresses(otpId: Int) {
        val request = UserAddressRequest(id = otpId)
        Toast.makeText(this, "id:$request", Toast.LENGTH_SHORT).show()

        RetrofitInstance.apiInterface.userAddress(request).enqueue(object : Callback<List<List<UserAddressResponseSubListItem>>?> {
            override fun onResponse(
                call: Call<List<List<UserAddressResponseSubListItem>>?>,
                response: Response<List<List<UserAddressResponseSubListItem>>?>
            ) {
                Toast.makeText(this@ManageAddressActivity, "Success: ${response.code()}", Toast.LENGTH_SHORT).show()
                Log.d("DataFetchApi", "onResponse: $response")
                val userData: List<UserAddressResponseSubListItem> = response.body()!!.get(0)
                addressList.addAll(userData)
                UserAdapter = UserAddressAdapter(addressList) { addressId ->
                    deleteAddress(addressId)
                }
                recyclerView.adapter = UserAdapter
            }

            override fun onFailure(
                call: Call<List<List<UserAddressResponseSubListItem>>?>,
                t: Throwable
            ) {
                Log.e("Fetch Address Response", "Failure: ${t.message}")
            }
        })
    }

    private fun deleteAddress(addressId: Int) {
        val request = DeleteAddressRequest(address_id = addressId)

        RetrofitInstance.apiInterface.deleteAddress(request).enqueue(object : Callback<DeleteAddressResponse?> {
            override fun onResponse(
                call: Call<DeleteAddressResponse?>,
                response: Response<DeleteAddressResponse?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ManageAddressActivity, "Address deleted successfully", Toast.LENGTH_SHORT).show()
                    addressList.removeAll { it.address_id == addressId }
                    UserAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ManageAddressActivity, "Failed to delete address", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteAddressResponse?>, t: Throwable) {
                Toast.makeText(this@ManageAddressActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
