package com.example.barkodershopapp.data.db.listdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity

@Entity(tableName = "list_table")
data class ListDataEntity (
    @ColumnInfo(name = "list_products")
    var listProducts : ProductDataEntity,
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
        )