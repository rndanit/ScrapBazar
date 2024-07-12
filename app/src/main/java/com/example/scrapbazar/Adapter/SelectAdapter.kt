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
import com.example.scrapbazar.DataModel.SelectDataClassSubListItem
import com.example.scrapbazar.R
import com.google.android.material.card.MaterialCardView

class SelectAdapter(
    private val context: Context,
    private val selectData: List<SelectDataClassSubListItem>?,
    private val onItemClick: (String) -> Unit // Change this to pass the selected volume
) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var selectedVolume: String? = null // Add this to keep track of the selected volume

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectItem = selectData?.get(position)
        if (selectItem != null) {
            holder.bind(selectItem)
        }

        holder.radioButton.isChecked = position == selectedPosition
        holder.cardView.strokeColor =
            if (position == selectedPosition) context.getColor(R.color.green)
            else context.getColor(R.color.white)
    }

    override fun getItemCount(): Int {
        return selectData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val volume: TextView = itemView.findViewById(R.id.volume)
        val cardView: MaterialCardView = itemView.findViewById(R.id.selectCardview)
        val radioButton: RadioButton = itemView.findViewById(R.id.SelectRadioButton)

        fun bind(selectItem: SelectDataClassSubListItem?) {
            if (selectItem != null) {
                volume.text = selectItem.volume
            }
        }

        //Functionality of a select the volume cardView
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    selectedVolume = selectData?.get(selectedPosition)?.volume // Update the selected volume
                    notifyItemChanged(selectedPosition)
                    onItemClick(selectedVolume ?: "") // Pass the selected volume back to the activity
                }
            }
        }
    }
}
