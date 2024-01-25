package com.example.barkodershopapp.data.db.listdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.barkodershopapp.ui.typeconverters.ProductConverter

@Database(entities = [ListDataEntity::class], version = 17)
@TypeConverters(ProductConverter::class)
abstract class ListDatabase : RoomDatabase() {

    abstract fun listDao() : ListDao

    companion object {

        @Volatile
        var INSTANCE: ListDatabase? = null

        fun getListInstance(context: Context): ListDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListDatabase::class.java,
                    "list_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance


            }


        }
    }
}