package com.example.barkodershopapp.data.db.pricedb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class PriceHistory (
    var lastPrice : String,
    var dataChangedPrice : String,
    var imageArrow : Int,
        )