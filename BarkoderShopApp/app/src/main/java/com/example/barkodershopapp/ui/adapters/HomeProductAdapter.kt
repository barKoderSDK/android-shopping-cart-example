package com.example.barkodershopapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.HomeProductListBinding
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.fragments.HistoryListFragmentDirections
import com.example.barkodershopapp.ui.fragments.HomeScreenFragmentDirections
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.example.barkodershopapp.ui.viewmodels.ListViewModel

class HomeProductAdapter (private var list: ArrayList<ProductDataEntity>,
                          private var viewModel: ListViewModel
) : RecyclerView.Adapter<HomeProductAdapter.ViewHolder>(){

    private var productListFull: List<ProductDataEntity> = ArrayList()

    init {
        productListFull = list
    }

    fun setProductsList2(productsList: ArrayList<ProductDataEntity>) {
        productListFull = productsList
        list = productsList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: HomeProductListBinding,
        private var viewModel: ListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ProductDataEntity) {
            binding.productNameHome.text = list.nameProduct
            binding.productBarcodeHome.text = list.barcodeProduct

            val byteArray = list.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageSelectProduct2.load(byteArray) {
                crossfade(true)
            }
            binding.constHomeProduct.setOnClickListener {
                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToProductHistoryFragment(list)
                Navigation.findNavController(binding.root).navigate(action,
                    NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getSelectInt(position: Int): ProductDataEntity {
        return list[position]
    }

    fun setProductsList(lista: ArrayList<ProductDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }
}