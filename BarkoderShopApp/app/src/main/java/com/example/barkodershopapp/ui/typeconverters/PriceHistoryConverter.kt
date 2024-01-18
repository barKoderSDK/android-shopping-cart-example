package com.example.barkodershopapp.ui.typeconverters

import androidx.room.TypeConverter
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PriceHistoryConverter {

    @TypeConverter
    @JvmStatic
    fun fromPriceHistoryList(value: ArrayList<PriceHistory>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<PriceHistory>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun toPriceHistoryList(value: String): ArrayList<PriceHistory> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<PriceHistory>>() {}.type
        return gson.fromJson(value, type)
    }
}