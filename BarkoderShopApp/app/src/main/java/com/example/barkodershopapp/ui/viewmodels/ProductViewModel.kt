package com.example.barkodershopapp.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.barkodershopapp.data.repositoriess.ProductDataRepository
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductDataRepository) :
    ViewModel() {

    var allNotes: LiveData<MutableList<ProductDataEntity>> = repository.allNotes
    fun getAllProducts(): LiveData<List<ProductDataEntity>> {
        return MutableLiveData<List<ProductDataEntity>>().apply {
            value = repository.getAllProducts()
        }
    }
    fun insert(list: ProductDataEntity) = viewModelScope.launch {
        repository.insert(list)
    }
    fun delete() = viewModelScope.launch {
        repository.delete()
    }
    fun deleteItem(list: ProductDataEntity) = viewModelScope.launch {
        repository.deleteItem(list)
    }
    fun getItem(id: Long) = viewModelScope.launch {
        repository.getItem(id)
    }
    fun updateItem(list: ProductDataEntity) = viewModelScope.launch {
        repository.updateItem(list)
    }

    var capturedPhotoBitmap: Bitmap? = null

}