package com.example.scrapbazar.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Adapter.ProductAdapter
import com.example.scrapbazar.Api.RetrofitInstance
import com.example.scrapbazar.DataModel.ProductDataClass
import com.example.scrapbazar.R
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    //Varibales are define

    private lateinit var mobileNumber: String
    private lateinit var searchView: SearchView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var continueButton: Button
    lateinit var notificationBell: ImageView
    lateinit var whatsappIcon: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var selectedProductNames: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //searchView=findViewById(R.id.searchView)

        adapter = ProductAdapter(emptyList(), this) { updateContinueButton() }

        mobileNumber = intent.getStringExtra("mobile_number").toString()

        continueButton = findViewById(R.id.continue_Btn)
        continueButton.setOnClickListener {
            selectedProductNames = adapter.getSelectedProductsName()
            val intent = Intent(this@HomeActivity, SelectApproximateActivity::class.java)
            intent.putStringArrayListExtra("selectedProductNames", selectedProductNames)
            intent.putExtra("mobile_number", mobileNumber)
            startActivity(intent)
        }

        //find the value of a variables.
        notificationBell = findViewById(R.id.notificationbell)
        whatsappIcon = findViewById(R.id.whatsappIcon)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        continueButton.isEnabled = false
        //continueButton.setBackgroundColor(Color.GRAY)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        getData()

        notificationBell.setOnClickListener {
            val intent = Intent(this@HomeActivity, MyNotificationActivity::class.java)
            startActivity(intent)
        }
        whatsappIcon.setOnClickListener {
            openWhatsApp()
        }

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        showCustomAlertDialog(this)


        //Navigation Drawer Functionality
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    Home()
                    true
                }
                R.id.my_request -> {
                    Toast.makeText(this, "My Request", Toast.LENGTH_SHORT).show()
                    myrequest()
                    true
                }
                R.id.notification -> {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    notification()
                    true
                }
                R.id.society -> {
                    Toast.makeText(this, "Society", Toast.LENGTH_SHORT).show()
                    society()
                    true
                }
                R.id.need_help -> {
                    Toast.makeText(this, "Need Help", Toast.LENGTH_SHORT).show()
                    help()
                    true
                }
                R.id.share -> {
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                    shareContent()
                    true
                }

                else -> false
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)

        // Find header view and set click listener for "View Profile"
        val headerView = navigationView.getHeaderView(0)
        val viewProfile = headerView.findViewById<TextView>(R.id.ViewProfile)
        viewProfile.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun Home() {
        val intent = Intent(this@HomeActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun help() {
            val intent = Intent(this@HomeActivity, NeedHelpActivity::class.java)
            startActivity(intent)
    }

    private fun updateContinueButton() {
        val selectedCount = adapter.getSelectedProductsName().size
        if (selectedCount > 0) {
            continueButton.isEnabled = true
            continueButton.backgroundTintList=ContextCompat.getColorStateList(this, R.color.green)
            continueButton.setBackgroundResource(R.drawable.proceedbutonshape)
           // binding.continueAddressBtn.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green)
        } else {
            continueButton.isEnabled = false
            continueButton.backgroundTintList=ContextCompat.getColorStateList(this,R.color.gray)
            continueButton.setBackgroundResource(R.drawable.proceedbutonshape)
        }
    }

    //Whatsapp open functionality
    private fun openWhatsApp() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/7359094774")
        startActivity(intent)
    }


    private fun society() {
        val intent = Intent(this@HomeActivity, SocietyCollabrationActivity::class.java)
        startActivity(intent)
    }

    private fun notification() {
        val intent = Intent(this@HomeActivity, MyNotificationActivity::class.java)
        startActivity(intent)
    }

    private fun myrequest() {
        val intent = Intent(this@HomeActivity, MyPickupResquestActivity::class.java)
        startActivity(intent)
    }

    //Share the the Content Functionality
    private fun shareContent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text to share")

        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //Alert Message Functionality.
    private fun showCustomAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("        Minimum Scrap Requirements        ")
        builder.setMessage("10 KG of scrap or Scrap Items with a total value at least Rs 150. Else pickup charge of Rs 40 will be applied.")
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
            setBackgroundColor(Color.GREEN)
            setTextColor(Color.WHITE)
        }
    }

    //GetData Function.
    private fun getData() {
        RetrofitInstance.apiInterface.getData()
            .enqueue(object : Callback<List<List<ProductDataClass>>?> {
                override fun onResponse(
                    call: Call<List<List<ProductDataClass>>?>,
                    response: Response<List<List<ProductDataClass>>?>
                ) {
                    if (response.isSuccessful) {
                        val productResponse: List<ProductDataClass> = response.body()!!.get(0)
                        adapter = ProductAdapter(productResponse, this@HomeActivity) { updateContinueButton() }
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@HomeActivity, "Not Success: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<List<ProductDataClass>>?>, t: Throwable) {
                    Log.d("Error", "onFailure:${t.localizedMessage} ")
                    Toast.makeText(this@HomeActivity, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
