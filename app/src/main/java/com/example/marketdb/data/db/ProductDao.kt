package com.example.marketdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.util.Constants

@Dao
interface ProductDao {

    //Funciones para interactuar con la base de datos

    //Create
    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Insert
    suspend fun insertProducts(products: MutableList<ProductEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_PRODUCT_TABLE}")
    suspend fun getAllProducts(): MutableList<ProductEntity>

    //Update
    @Update
    suspend fun updateProduct(product: ProductEntity)

    //Delete
    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}


