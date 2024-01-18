package com.example.barkodershopapp.data.repositoriess

import androidx.lifecycle.LiveData
import com.example.barkodershopapp.data.db.productdatabase.ProductDao
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataRepository @Inject constructor(private val dao: ProductDao) {

    var allNotes: LiveData<MutableList<ProductDataEntity>> = dao.getAll()
    fun getAllProducts(): List<ProductDataEntity> {
        return dao.getItems()
    }

    suspend fun insert(list: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(list)
        }
    }

    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.delete()
        }
    }

    suspend fun deleteItem(lsit: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(lsit)
        }
    }

    suspend fun getItem(id: Long) {
        withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }

    suspend fun updateItem(list: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.updateItem(list)
        }
    }
}