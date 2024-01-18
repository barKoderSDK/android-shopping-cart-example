package com.example.barkodershopapp.data.repositoriess

import androidx.lifecycle.LiveData
import com.example.barkodershopapp.data.db.listdatabase.ListDao
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListDataRepository @Inject constructor(private val dao : ListDao){

    var allNotes: LiveData<MutableList<ListDataEntity>> = dao.getAll()
    fun getAllProducts(): List<ListDataEntity> {
        return dao.getItems()
    }

    suspend fun insert(list: ListDataEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(list)
        }
    }

    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.delete()
        }
    }

    suspend fun deleteItem(lsit: ListDataEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(lsit)
        }
    }

    suspend fun getItem(id: Long) {
        withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }

    suspend fun updateItem(list: ListDataEntity) {
        withContext(Dispatchers.IO) {
            dao.updateItem(list)
        }
    }

}