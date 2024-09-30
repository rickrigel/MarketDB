package com.example.marketdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.databinding.ProductElementBinding

class ProductAdapter (
        private val onProductClicked: (ProductEntity) -> Unit
    ): RecyclerView.Adapter<ProductViewHolder>() {

        private var products: MutableList<ProductEntity> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val binding = ProductElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ProductViewHolder(binding)
        }

        override fun getItemCount(): Int = products.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

            val product = products[position]

            holder.bind(product)

            //Para manejar el click al elemento
            holder.itemView.setOnClickListener {
                onProductClicked(product)
            }
        }

        fun updateList(list: MutableList<ProductEntity>){
            products = list
            notifyDataSetChanged()
        }

    }