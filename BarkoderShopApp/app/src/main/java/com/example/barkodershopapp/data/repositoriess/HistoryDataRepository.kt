package com.example.barkodershopapp.data.repositoriess

import androidx.lifecycle.LiveData
import com.example.barkodershopapp.data.db.historydatabase.HistoryDao
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryDataRepository @Inject constructor(private val dao: HistoryDao) {

    var allNotes : LiveData<MutableList<HistoryDataEntity>> = dao.getAll()
    suspend fun insert(list : HistoryDataEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(list)
        }
    }
    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.delete()
        }
    }
    suspend fun deleteItem(lsit : HistoryDataEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(lsit)
        }
    }
    suspend fun getItem(id: Long) {
        withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }
    suspend fun updateItem(list: HistoryDataEntity) {
        withContext(Dispatchers.IO) {
            dao.updateItem(list)
        }
    }
}