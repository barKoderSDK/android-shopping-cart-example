package com.example.barkodershopapp.data.db.historydatabase

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (list : HistoryDataEntity)

    @Query("SELECT * FROM history_table")
    fun getAll() : LiveData<MutableList<HistoryDataEntity>>

    @Query("DELETE FROM history_table")
    fun delete()

    @Delete
    fun deleteItem(list: HistoryDataEntity)

    @Query("SELECT * FROM history_table WHERE id = :itemId")
    fun getItem(itemId: Long): List<HistoryDataEntity>

    @Update
    fun updateItem(list: HistoryDataEntity)

}