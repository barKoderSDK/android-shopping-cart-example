package com.example.barkodershopapp.data.db.productdatabase

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (list: ProductDataEntity)
    @Query("SELECT * FROM product_table")
    fun getItems() : List<ProductDataEntity>

    @Query("SELECT * FROM product_table")
    fun getAll() :  LiveData<MutableList<ProductDataEntity>>

    @Query("DELETE FROM product_table")
    fun delete ()

    @Delete
    fun deleteItem(list: ProductDataEntity)

    @Query("SELECT * FROM product_table WHERE id = :itemId")
    fun getItem(itemId: Long): ProductDataEntity

    @Update
    fun updateItem(note: ProductDataEntity)
}