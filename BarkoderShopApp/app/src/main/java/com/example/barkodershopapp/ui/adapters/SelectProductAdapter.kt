package com.example.barkodershopapp.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.fragments.SelectProductFragmentDirections
import com.example.barkodershopapp.ui.listeners.onClickAddProduct
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import java.util.*
import kotlin.collections.ArrayList

class SelectProductAdapter(
    private var list: ArrayList<ProductDataEntity>,
    private var viewModel: ListViewModel,
    private var listId : Long,
    private var checkedDate : String,
    private var listName : String,
    private var listener : onClickAddProduct
) : RecyclerView.Adapter<SelectProductAdapter.ViewHolder>(), Filterable {

    private var productListFull: List<ProductDataEntity> = ArrayList()

    var showAddButton = false
    var isEditMode = false
    init {
        productListFull = list
    }

    fun setProductsList2(productsList: ArrayList<ProductDataEntity>) {
        productListFull = productsList
        list = productsList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: SelectproductItemBinding,
        private var viewModel: ListViewModel,
        private var listener : onClickAddProduct
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ProductDataEntity, showAddButton : Boolean, isEditMode : Boolean, listId : Long, checkedDate : String,
                 listName : String) {
            binding.textSelectProductName.text = list.nameProduct
            binding.textSelectProductBarcode.text = list.barcodeProduct
            binding.textSelectProductPrice.text = list.priceProduct.toString() + " $"
            binding.selectLayout.setOnClickListener {
                try {


                    val actions2 = SelectProductFragmentDirections.actionSelectProductFragmentToProductHistoryFragment(list)
                    Navigation.findNavController(binding.root).navigate(actions2)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.textProductUnit.text = list.unitProduct
            binding.textQuanitityProduct.text = list.quantityProduct.toString()

            binding.btnAddProduct.setOnClickListener {
                viewModel.insert(ListDataEntity(list))
                listener.onAdedProduct(list)
            }


            if(showAddButton) {
                binding.btnAddProduct.visibility = View.VISIBLE
                if(list.adedProduct == true){
                    binding.btnAddProduct.visibility = View.GONE
                    binding.btnAddedProduct.visibility = View.VISIBLE
                } else {
                    binding.btnAddProduct.visibility = View.VISIBLE
                    binding.btnAddedProduct.visibility = View.GONE
                }
            } else {
                binding.btnAddProduct.visibility = View.GONE
            }

            val byteArray = list.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageSelectProduct2.load(byteArray) {
                crossfade(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SelectproductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewModel, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], showAddButton, isEditMode, listId, checkedDate, listName)
    }

    fun getSelectInt(position: Int): ProductDataEntity {
        return list[position]
    }

    fun setProductsList(lista: ArrayList<ProductDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<ProductDataEntity>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(productListFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (product in productListFull) {
                        if (product.nameProduct?.toLowerCase(Locale.ROOT)!!
                                .contains(filterPattern)
                        ) {
                            filteredList.add(product)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list.clear()
                list.addAll(results?.values as ArrayList<ProductDataEntity>)
                notifyDataSetChanged()
            }
        }
    }
}