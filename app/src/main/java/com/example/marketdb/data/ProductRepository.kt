package com.example.marketdb.data

import com.example.marketdb.data.db.ProductDao
import com.example.marketdb.data.db.model.ProductEntity

class ProductRepository (
    private val productDao: ProductDao
) {

    suspend fun insertProduct(product: ProductEntity){
        productDao.insertProduct(product)
    }

    suspend fun getAllProducts(): MutableList<ProductEntity> = productDao.getAllProducts()

    suspend fun updateProduct(product: ProductEntity){
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity){
        productDao.deleteProduct(product)
    }

}