package com.example.barkodershopapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.CurrentlistListBinding
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.listeners.OnCheckedListener
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import okhttp3.internal.notify

class CurrentListAdapter(private var list : List<ListDataEntity>, private val productViewModel : ProductViewModel,
private val listener : OnCheckedListener) : RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {

    var showCheckboxes = false




    class ViewHolder(private val binding: CurrentlistListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(list: ListDataEntity, showCheckboxes: Boolean, productViewModel: ProductViewModel, listener : OnCheckedListener) {

            binding.textProductName.text = list.listProducts.nameProduct
            binding.textProductPrice.text = list.listProducts.priceProduct.toString()
            binding.textProductBarcode.text = list.listProducts.barcodeProduct
            binding.textCountPr.text = list.listProducts.count.toString()
            binding.textScannedCount.text = list.listProducts.defultCount.toString()
            binding.textProductTotalPrice.text = list.listProducts.totalPrice.toString()
            binding.textView12.text = list.listProducts.count.toString()
            val byteArray = list.listProducts.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageProduct2.load(byteArray) {
                crossfade(true)
            }
            binding.checkBox.isChecked = list.listProducts.checkout

            binding.checkBox.setOnClickListener {

                listener.onCheckedChanged(list)

            }

            if (showCheckboxes) {
                binding.checkBox.visibility = View.VISIBLE
                binding.textCountPr.visibility = View.VISIBLE
                binding.textScannedCount.visibility = View.VISIBLE
                binding.textView6.visibility = View.VISIBLE
                binding.textView3.visibility = View.GONE
                binding.textView12.visibility = View.GONE
            } else {
                binding.checkBox.visibility = View.GONE
                binding.textCountPr.visibility = View.GONE
                binding.textScannedCount.visibility = View.GONE
                binding.textView6.visibility = View.GONE
                binding.textView3.visibility = View.VISIBLE
                binding.textView12.visibility = View.VISIBLE
            }


        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            CurrentlistListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], showCheckboxes, productViewModel, listener)
//        notifyItemChanged(position)



    }

    override fun getItemCount(): Int = list.size

    fun setNotesList(lista: List<ListDataEntity>) {
        this.list = lista
        notifyDataSetChanged()

    }

    fun getHistoryInt(position: Int): ListDataEntity {
        return list[position]
    }

    fun updateItemAtPosition(position: Int) {
        list[position].listProducts.defultCount = list[position].listProducts.count
        notifyItemChanged(position)
    }

}


