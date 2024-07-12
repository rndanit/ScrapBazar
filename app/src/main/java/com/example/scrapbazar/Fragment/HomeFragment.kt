package com.example.scrapbazar.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.Activity.HomeActivity
import com.example.scrapbazar.Adapter.ProductAdapter
import com.example.scrapbazar.DataModel.ProductDataClass
import com.example.scrapbazar.R
import com.example.scrapbazar.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
       // getData()

        return view
    }
}


    /*
    //Function of fetch the data from API
    private fun getData() {

        RetrofitInstance.apiInterface.getData()
            .enqueue(object : Callback<List<List<ProductDataClass>>?> {
                override fun onResponse(
                    call: Call<List<List<ProductDataClass>>?>,
                    response: Response<List<List<ProductDataClass>>?>
                ) {
                    if (response.isSuccessful) {

                        val productResponse: List<ProductDataClass> = response.body()!!.get(0)

                        adapter = ProductAdapter(productResponse, requireContext())
                        recyclerView.adapter = adapter

                    } else {
                        Toast.makeText(
                            context,
                            "Not Success: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<List<ProductDataClass>>?>, t: Throwable) {
                    Log.d("Error", "onFailure:${t.localizedMessage} ")
                    Toast.makeText(context, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })

    }

}
     */
