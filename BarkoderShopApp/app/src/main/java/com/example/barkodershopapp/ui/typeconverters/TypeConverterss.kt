package com.example.barkodershopapp.ui.typeconverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream


object TypeConverterss {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap {
      return  BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.size)
    }
    @TypeConverter
    @JvmStatic
    fun fromString(value : String?) : ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    @JvmStatic
    fun fromArrayList(list : ArrayList<String?>): String {
        return Gson().toJson(list)
    }
}