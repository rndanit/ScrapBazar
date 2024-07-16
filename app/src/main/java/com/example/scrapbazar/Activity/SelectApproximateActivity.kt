package com.example.scrapbazar.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Adapter.SelectAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.SelectDataClassSubListItem
import com.example.scrapbazar.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectApproximateActivity : AppCompatActivity() {

    private lateinit var mobileNumber: String
    private lateinit var selectedProducts:List<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectAdapter
    private lateinit var button: Button
    private var selectedVolume: String? = null // Add this to store the selected volume

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_approximate)

        //Mobile number data get
        mobileNumber = intent.getStringExtra("mobile_number").toString()

        //Product name data get
        selectedProducts = intent.getStringArrayListExtra("selectedProductNames")!!

        val selectedProductNames = intent.getStringArrayListExtra("selectedProductNames")
        Toast.makeText(this, "Received product names: $selectedProductNames", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Received : $selectedProducts", Toast.LENGTH_SHORT).show()

        // Set button to default disabled state
        button = findViewById(R.id.continue_Btn)

        button.isEnabled = false


        recyclerView = findViewById(R.id.recyclerView_second)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        getData()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Customize the toolbar
        supportActionBar?.title = "Select Approximate Weight" // Set the toolbar title
        toolbar.setTitleTextColor(getColor(R.color.white)) // Set the title color

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Set the toolbar background color (optional)
        toolbar.setBackgroundColor(getColor(R.color.colorPrimary))

        //Button to perform the intent functionality
        button.setOnClickListener {
            // Pass the selected volume to the next activity
            val intent = Intent(this@SelectApproximateActivity, SelectPickupAddressActivity::class.java)
            intent.putExtra("selected_volume", selectedVolume)
            intent.putExtra("mobile_number",mobileNumber)
            intent.putStringArrayListExtra("selectedProductNames", selectedProductNames)
            startActivity(intent)
            Toast.makeText(this@SelectApproximateActivity,"${selectedVolume}",Toast.LENGTH_SHORT).show()
        }
    }

    //Get Data Function to perform the fetch a data from the Api.
    private fun getData() {
        RetrofitInstance.apiInterface.selectData().enqueue(object : Callback<List<List<SelectDataClassSubListItem>>?> {
            override fun onResponse(
                call: Call<List<List<SelectDataClassSubListItem>>?>,
                response: Response<List<List<SelectDataClassSubListItem>>?>
            ) {
                Toast.makeText(this@SelectApproximateActivity, "Success: ${response.code()}", Toast.LENGTH_SHORT).show()
                Log.d("DataFetchApi", "onResponse: $response")
                val productResponse: List<SelectDataClassSubListItem> = response.body()!!.get(0)
                adapter = SelectAdapter(applicationContext, productResponse) { selectedVolume ->
                    // Enable and change button color when an item is clicked
                    button.isEnabled = true
                    button.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green)
                    this@SelectApproximateActivity.selectedVolume = selectedVolume // Store the selected volume
                }
                recyclerView.adapter = adapter

            }

            override fun onFailure(
                call: Call<List<List<SelectDataClassSubListItem>>?>,
                t: Throwable
            ) {
                Log.d("Error", "onFailure:${t.localizedMessage} ")
                Toast.makeText(this@SelectApproximateActivity, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
