package com.example.barkodershopapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.HomeListListBinding
import com.barkoder.shoppingApp.net.databinding.HomeProductListBinding
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.fragments.HistoryListFragmentDirections
import com.example.barkodershopapp.ui.fragments.HomeScreenFragmentDirections
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.example.barkodershopapp.ui.viewmodels.ListViewModel

class HomeListAdapter (private var list: ArrayList<HistoryDataEntity>,
                       private var viewModel: ListViewModel
) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>(){

    private var productListFull: List<HistoryDataEntity> = ArrayList()

    init {
        productListFull = list
    }

    fun setProductsList2(productsList: ArrayList<HistoryDataEntity>) {
        productListFull = productsList
        list = productsList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: HomeListListBinding,
        private var viewModel: ListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: HistoryDataEntity) {
            binding.listNameHome.text = list.listName
            binding.listSizeHome.text = list.listProducts.size.toString()
            binding.listCircleText.text = list.listName.first().toUpperCase().toString()
            binding.recViewHomeList.setOnClickListener {
                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToCurrentListFragment2(list)
                Navigation.findNavController(binding.root).navigate(action)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeListListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getSelectInt(position: Int): HistoryDataEntity {
        return list[position]
    }

    fun setProductsList(lista: ArrayList<HistoryDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }
}