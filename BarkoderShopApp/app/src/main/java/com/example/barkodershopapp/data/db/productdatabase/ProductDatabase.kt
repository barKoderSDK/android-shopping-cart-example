package com.example.barkodershopapp.data.db.productdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss


@Database(entities = [ProductDataEntity::class], version = 42)
@TypeConverters(TypeConverterss::class)
abstract class ProductDatabase : RoomDatabase(){

    abstract fun productDao() : ProductDao


    companion object {

        @Volatile
        var INSTANCE: ProductDatabase? = null


        fun getProductInstance(context: Context): ProductDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance


            }


        }
    }
}