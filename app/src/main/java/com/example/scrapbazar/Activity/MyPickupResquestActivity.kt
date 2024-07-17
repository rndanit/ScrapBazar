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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.scrapbazar.Adapter.RequestAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.RequestQueryRequest
import com.example.scrapbazar.DataModel.RequestQueryResponseSubListItem
import com.example.scrapbazar.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPickupResquestActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var progessBar: ProgressBar
    private lateinit var progessBarTextView:TextView
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pickup_resquest)

        //Find the Id of a Variables.
        progessBar=findViewById(R.id.progressBar)
        progessBarTextView=findViewById(R.id.progressTextview)
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)


        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "My Pickup Request"
        toolbar.setTitleTextColor(getColor(R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        // Show progress bar and text
        progessBarTextView.visibility = View.VISIBLE
        progessBar.visibility = View.VISIBLE

        recyclerView = findViewById(R.id.requestRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {

            val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val otpId = sharedPreference.getInt("otp_id", -1)

            if (otpId != -1) {
                fetchRequestData(otpId)
            } else {
                Toast.makeText(this, "Failed to retrieve OTP ID", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }

        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val otpId = sharedPreference.getInt("otp_id", -1)

        if (otpId != -1) {
            fetchRequestData(otpId)
        } else {
            Toast.makeText(this, "Failed to retrieve OTP ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchRequestData(otpId: Int) {
        val request = RequestQueryRequest(id = otpId)
        Toast.makeText(this, "id:$request", Toast.LENGTH_SHORT).show()

        RetrofitInstance.apiInterface.userQuery(request).enqueue(object : Callback<List<List<RequestQueryResponseSubListItem>>?> {
            override fun onResponse(
                call: Call<List<List<RequestQueryResponseSubListItem>>?>,
                response: Response<List<List<RequestQueryResponseSubListItem>>?>
            ) {
                Toast.makeText(this@MyPickupResquestActivity, "Success: ${response.code()}", Toast.LENGTH_SHORT).show()
                Log.d("DataFetchApi", "onResponse: $response")
                val requestData: List<RequestQueryResponseSubListItem> = response.body()!!.get(0)
                requestAdapter = RequestAdapter(requestData) { selectedItem ->
                    val intent = Intent(this@MyPickupResquestActivity, PickupStatusActivity::class.java).apply {
                        putExtra("REQUEST_ID", selectedItem.primary_id)
                        putExtra("REQUEST_DATE", selectedItem.date)
                        putExtra("Color_Code", selectedItem.colorcode)
                        putExtra("Request_Status",selectedItem.status)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = requestAdapter

                //  Not Show progress bar and text
                progessBarTextView.visibility = View.GONE
                progessBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing=false

                Toast.makeText(this@MyPickupResquestActivity, "RequestData:${requestData}", Toast.LENGTH_SHORT).show()
                Log.d("DataFetchApi", "Request Data:${requestData}")
            }

            override fun onFailure(
                call: Call<List<List<RequestQueryResponseSubListItem>>?>,
                t: Throwable
            ) {
                // Handle failure
                //  Not Show progress bar and text
                progessBarTextView.visibility = View.GONE
                progessBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing=false
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
