package com.example.stolker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Product
import com.example.stolker.R

class ProductsAdapter(
    private val products: ArrayList<Product>,
    private val onCLick: (Product) -> Unit
): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View, val onCLick: (Product) -> Unit):
        RecyclerView.ViewHolder(itemView) {

        private val productName: TextView = itemView.findViewById(R.id.textview_product_name)
        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let {
                    onCLick(it)
                }
            }
        }

        fun bind(product: Product) {
            currentProduct = product

            productName.text = product.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
            .also {
                return ViewHolder(it, onCLick)
            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

}