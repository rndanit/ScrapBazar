package com.example.scrapbazar.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scrapbazar.DataModel.UserResponseSubListItem
import com.example.scrapbazar.R
import com.google.android.material.card.MaterialCardView

class AddressAdapter(
    private val context: Context,
    private val UserData: List<UserResponseSubListItem>?,
    private val onItemClick: (UserResponseSubListItem) -> Unit
) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectItem = UserData?.get(position)
        if (selectItem != null) {
            holder.bind(selectItem)
        }
        // Bind data here (e.g., holder.cardView.someTextView.text = addressList[position].someData)
        // Handle selection
        holder.radioButton.isChecked = position == selectedPosition
        holder.cardView.strokeColor =
            if (position == selectedPosition) context.getColor(R.color.green)
            else context.getColor(R.color.white)

    }

    override fun getItemCount(): Int {
        return UserData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstName:TextView=itemView.findViewById(R.id.firstName)
        private val lastName:TextView=itemView.findViewById(R.id.lastName)
        private val landMark:TextView=itemView.findViewById(R.id.landmarkName)
        private val addressName:TextView=itemView.findViewById(R.id.addressName)
        private val stateName:TextView=itemView.findViewById(R.id.stateName)
        private val cityName:TextView=itemView.findViewById(R.id.cityName)
        private val pinCode:TextView=itemView.findViewById(R.id.pinCode)
        val cardView: MaterialCardView = itemView.findViewById(R.id.addressCardview)
        val radioButton: RadioButton = itemView.findViewById(R.id.addressRadioButton)


        fun bind(selectItem: UserResponseSubListItem?) {
            if (selectItem != null) {

                firstName.text=selectItem.first_name
                lastName.text=selectItem.last_name
                landMark.text=selectItem.landmark
                addressName.text=selectItem.address
                stateName.text=selectItem.state
                cityName.text=selectItem.city
                pinCode.text= selectItem.pincode.toString()
            }
        }
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                    onItemClick(UserData?.get(adapterPosition)!!)
                }
            }
        }

    }
}

