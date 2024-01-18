package com.example.barkodershopapp.ui.typeconverters

import androidx.room.TypeConverter
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object ProductConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): ProductDataEntity? {
        val gson = Gson()
        val type: Type = object : TypeToken<ProductDataEntity?>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    @JvmStatic
    fun fromProductDataEntity(productDataEntity: ProductDataEntity?): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<ProductDataEntity?>() {}.type
        return gson.toJson(productDataEntity, type)
    }
}