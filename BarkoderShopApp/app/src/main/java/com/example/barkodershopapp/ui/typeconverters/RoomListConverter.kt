package com.example.barkodershopapp.ui.typeconverters

import androidx.room.TypeConverter
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object RoomListConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): ArrayList<ListDataEntity?>? {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<ListDataEntity?>?>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    @JvmStatic
    fun fromArrayList(listDataEntity: ArrayList<ListDataEntity?>?): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<ListDataEntity?>?>() {}.type
        return gson.toJson(listDataEntity, type)
    }
}