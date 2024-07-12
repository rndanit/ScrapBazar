package com.example.scrapbazar.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.DataModel.RequestQueryResponseSubListItem
import com.example.scrapbazar.R

class RequestAdapter(
    private val requestData: List<RequestQueryResponseSubListItem>,
    private val itemClickListener: (RequestQueryResponseSubListItem) -> Unit
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pickup_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectItem = requestData[position]
        holder.bind(selectItem, position)
    }

    override fun getItemCount(): Int {
        return requestData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val requestId: TextView = itemView.findViewById(R.id.requestId)
        private val addressId: TextView = itemView.findViewById(R.id.addressId)
        private val dateId: TextView = itemView.findViewById(R.id.dateId)
        private val requestStatusId: TextView = itemView.findViewById(R.id.RequestStatusId)

        fun bind(selectItem: RequestQueryResponseSubListItem, position: Int) {
            requestId.text = "#${selectItem.primary_id}"
            addressId.text = selectItem.address
            dateId.text = selectItem.date
            requestStatusId.text = selectItem.status

            // Logic to change text color when status is "Cancelled"
            if (selectItem.status == "Cancelled") {
                requestStatusId.setTextColor(Color.parseColor(selectItem.colorcode))
            }else{
                requestStatusId.setTextColor(Color.parseColor(selectItem.colorcode))
            }

            itemView.setOnClickListener {
                itemClickListener(selectItem)
                notifyItemChanged(position)
            }
        }
    }
}
/*
package com.example.scrapbazar.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.DataModel.RequestQueryResponseSubListItem
import com.example.scrapbazar.R

class RequestAdapter(
    private val requestData: List<RequestQueryResponseSubListItem>,
    private val itemClickListener: (RequestQueryResponseSubListItem) -> Unit
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pickup_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectItem = requestData[position]
        holder.bind(selectItem)
    }

    override fun getItemCount(): Int {
        return requestData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val requestId: TextView = itemView.findViewById(R.id.requestId)
        private val addressId: TextView = itemView.findViewById(R.id.addressId)
        private val dateId: TextView = itemView.findViewById(R.id.dateId)
        private val requestStatusId: TextView = itemView.findViewById(R.id.RequestStatusId)

        fun bind(selectItem: RequestQueryResponseSubListItem) {
            requestId.text = "#${selectItem.primary_id}"
            addressId.text = selectItem.address
            dateId.text = selectItem.date
            requestStatusId.text = selectItem.status

            // Apply the color code based on the status
            when (selectItem.status) {
                "Request Placed" -> {
                    requestStatusId.setBackgroundColor(Color.parseColor(selectItem.colorcode))
                }
                "Cancelled" -> {
                    requestStatusId.setBackgroundColor(Color.RED)
                }
                else -> {
                    requestStatusId.setBackgroundColor(Color.parseColor(selectItem.colorcode))
                }
            }

            itemView.setOnClickListener {
                itemClickListener(selectItem)
            }
        }
    }
}

 */