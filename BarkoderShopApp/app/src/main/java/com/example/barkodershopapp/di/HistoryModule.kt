package com.example.barkodershopapp.di

import android.app.Application
import com.example.barkodershopapp.data.db.historydatabase.HistoryDao
import com.example.barkodershopapp.data.db.historydatabase.HistoryDatabase
import com.example.barkodershopapp.data.db.listdatabase.ListDao
import com.example.barkodershopapp.data.db.listdatabase.ListDatabase
import com.example.barkodershopapp.data.db.productdatabase.ProductDao
import com.example.barkodershopapp.data.db.productdatabase.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Singleton
    @Provides
    fun getHistoryDB(context: Application): HistoryDatabase {
        return HistoryDatabase.getHistoryInstance(context)
    }

    @Singleton
    @Provides
    fun getHistoryDao(historyDB: HistoryDatabase): HistoryDao {
        return historyDB.historyDao()
    }

    @Singleton
    @Provides
    fun getProductDB(context: Application): ProductDatabase {
        return ProductDatabase.getProductInstance(context)
    }

    @Singleton
    @Provides
    fun getProductDao(productDB: ProductDatabase): ProductDao {
        return productDB.productDao()
    }

    @Singleton
    @Provides
    fun getListDB(context: Application): ListDatabase {
        return ListDatabase.getListInstance(context)
    }

    @Singleton
    @Provides
    fun getListDao(listDB: ListDatabase): ListDao {
        return listDB.listDao()
    }


}