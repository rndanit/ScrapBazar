package com.example.scrapbazar.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scrapbazar.DataModel.ProductDataClass
import com.example.scrapbazar.R

class ProductAdapter(
    private val productList: List<ProductDataClass>,
    private val context: Context,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val selectedProductsName = ArrayList<String>()

    fun getSelectedProductsName(): ArrayList<String> {
        return selectedProductsName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = productList[position]
        holder.bind(productItem)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val cardView: CardView = itemView.findViewById(R.id.maincardView)

        fun bind(product: ProductDataClass) {
            val names: String = product.product_name

            productName.text = product.product_name
            productPrice.text = product.product_price
            Glide.with(itemView.context).load(product.product_image).into(productImage)

            if (selectedProductsName.contains(names)) {
                cardView.setBackgroundResource(R.drawable.card_border_selected)
            } else {
                cardView.setBackgroundResource(R.drawable.card_border)
            }

            cardView.setOnClickListener {
                if (selectedProductsName.contains(names)) {
                    selectedProductsName.remove(names)
                } else {
                    selectedProductsName.add(names)
                }
                notifyDataSetChanged()
                onSelectionChanged()
            }
        }
    }
}
