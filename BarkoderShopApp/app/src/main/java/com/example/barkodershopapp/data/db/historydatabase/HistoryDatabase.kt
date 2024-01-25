package com.example.barkodershopapp.data.db.historydatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.barkodershopapp.ui.typeconverters.RoomListConverter

@Database(entities = [HistoryDataEntity::class], version = 39)
@TypeConverters(RoomListConverter::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    companion object {

            @Volatile
            var INSTANCE: HistoryDatabase? = null

            fun getHistoryInstance(context: Context): HistoryDatabase {
                var tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    var instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                    return instance


                }


            }
        }

    }
