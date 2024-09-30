package com.example.marketdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.databinding.ProductElementBinding

class ProductViewHolder (
    private val binding: ProductElementBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductEntity){

            binding.apply {
                tvTitle.text = product.title
                tvStatus.text = product.status
                tvBrand.text = product.brand
            }
        }
    }