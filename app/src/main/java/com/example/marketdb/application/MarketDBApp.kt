package com.example.marketdb.application

import android.app.Application
import com.example.marketdb.data.ProductRepository
import com.example.marketdb.data.db.ProductDatabase

class MarketDBApp: Application() {
    private val database by lazy{
        ProductDatabase.getDatabase(this@MarketDBApp)
    }

    val repository by lazy {
        ProductRepository(database.productDao())
    }
}