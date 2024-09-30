package com.example.marketdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.marketdb.util.Constants

@Entity(tableName = Constants.DATABASE_PRODUCT_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Long = 0,
    @ColumnInfo(name = "product_title")
    var title: String,
    @ColumnInfo(name = "product_status")
    var status: String,
    @ColumnInfo(name = "product_brand")
    var brand: String
)