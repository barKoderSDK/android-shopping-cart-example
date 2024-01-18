package com.example.barkodershopapp.data.db.listdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (list: ListDataEntity)
    @Query("SELECT * FROM list_table")
    fun getItems() : List<ListDataEntity>

    @Query("SELECT * FROM list_table")
    fun getAll() : LiveData<MutableList<ListDataEntity>>

    @Query("DELETE FROM list_table")
    fun delete ()

    @Delete
    fun deleteItem(list: ListDataEntity)

    @Query("SELECT * FROM list_table WHERE id = :itemId")
    fun getItem(itemId: Long): ListDataEntity

    @Update
    fun updateItem(note: ListDataEntity)
}