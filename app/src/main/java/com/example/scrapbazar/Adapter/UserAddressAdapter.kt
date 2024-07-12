package com.example.scrapbazar.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.DataModel.UserAddressResponseSubListItem
import com.example.scrapbazar.DataModel.UserResponseSubListItem
import com.example.scrapbazar.R
import com.google.android.material.card.MaterialCardView

 class UserAddressAdapter(private val UserAddress: List<UserAddressResponseSubListItem>?,
     private  val deleteAddressCallback:(Int) -> Unit ):
    RecyclerView.Adapter<UserAddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manageaddresslayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Item = UserAddress?.get(position)
        if (Item != null) {
            holder.bind(Item)
        }
        holder.deleteImage.setOnClickListener {
            Item?.address_id?.let { addressId -> deleteAddressCallback(addressId) }
        }

    }

    override fun getItemCount(): Int {
        return UserAddress!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val address:TextView=itemView.findViewById(R.id.addressData)
        private val state:TextView=itemView.findViewById(R.id.StateText)
        private val city:TextView=itemView.findViewById(R.id.cityText)
        private val pincode:TextView=itemView.findViewById(R.id.pincodeText)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleteImage)

        fun bind(selectItem: UserAddressResponseSubListItem?) {
            if (selectItem != null) {

                address.text=selectItem.address
                state.text=selectItem.state
                city.text=selectItem.city
                pincode.text=selectItem.pincode.toString()
            }
        }

    }
}
/*
class UserAddressAdapter(
    private val UserAddress: List<UserAddressResponseSubListItem>?,
    private val deleteAddressCallback: (Int) -> Unit // Callback for delete action
) : RecyclerView.Adapter<UserAddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manageaddresslayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Item = UserAddress?.get(position)
        if (Item != null) {
            holder.bind(Item)
        }

        holder.deleteImage.setOnClickListener {
            Item?.address_id?.let { addressId -> deleteAddressCallback(addressId) }
        }
    }

    override fun getItemCount(): Int {
        return UserAddress!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val address: TextView = itemView.findViewById(R.id.addressData)
        private val state: TextView = itemView.findViewById(R.id.StateText)
        private val city: TextView = itemView.findViewById(R.id.cityText)
        private val pincode: TextView = itemView.findViewById(R.id.pincodeText)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleteImage)

        fun bind(selectItem: UserAddressResponseSubListItem?) {
            if (selectItem != null) {
                address.text = selectItem.address
                state.text = selectItem.state
                city.text = selectItem.city
                pincode.text = selectItem.pincode.toString()
            }
        }
    }
}

 */

