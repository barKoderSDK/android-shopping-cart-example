package com.example.barkodershopapp.data.db.historydatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "history_table")
data class HistoryDataEntity (

    @ColumnInfo(name = "ListName")
     var listName : String,
    @ColumnInfo(name = "CreatedDate")
    var listDate : String,
    @ColumnInfo(name = "TotalCost")
    var totalCost : String,
    @ColumnInfo(name = "CheckedDate")
    var checkedDate : String,
    @ColumnInfo(name= "CheckedList")
    var checkedList : Boolean,
    @ColumnInfo(name = "List_Products")
    var listProducts : @RawValue ArrayList<ListDataEntity>,
    @PrimaryKey(autoGenerate = true)
      var id : Long = 0L,

    ) : Parcelable