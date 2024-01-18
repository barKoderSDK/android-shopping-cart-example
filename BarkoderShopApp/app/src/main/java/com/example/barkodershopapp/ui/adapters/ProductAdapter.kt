package com.example.barkodershopapp.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ProductListItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.barkodershopapp.ui.listeners.OnClickListenerButtons
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.squareup.picasso.Picasso
import java.io.ByteArrayInputStream

class ProductAdapter(private val context:Context,
    private var list: ArrayList<ListDataEntity>,
    private val listener: OnClickListenerButtons,
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var oldDataList = emptyList<ListDataEntity>()


    private val differCallback = object : DiffUtil.ItemCallback<ListDataEntity>(){
        override fun areItemsTheSame(oldItem: ListDataEntity, newItem: ListDataEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ListDataEntity,
            newItem: ListDataEntity
        ): Boolean {
            return oldItem == newItem
        }

    }


    private val differ = AsyncListDiffer(this, differCallback)


    class ViewHolder(
        private var binding: ProductListItemBinding,
        private val listener: OnClickListenerButtons
    ) : RecyclerView.ViewHolder(binding.root) {



        fun bind(list: ListDataEntity, context:Context) {
            binding.btnAddSize.setOnClickListener {
                listener.onClickPlus(list)
            }
            binding.btnMinusSize.setOnClickListener {
                listener.onClickMinus(list)
            }
            binding.textProductName.text = list.listProducts.nameProduct
            binding.textProductBarcode.text = list.listProducts.barcodeProduct.toString()
            binding.textCountProduct.text = list.listProducts.count.toString()
            binding.textProductPrice.text = list.listProducts.priceProduct.toString()
            binding.textProductTotalPrice.text = list.listProducts.totalPrice.toString()

            val byteArray = list.listProducts.imageProduct?.let { TypeConverterss.toBitmap(it) }

            binding.imageProduct2.load(byteArray) {
                crossfade(true)
                crossfade(1500)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int  {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], context)
    }

    fun setNotesList(lista: ArrayList<ListDataEntity>) {
        this.list = lista
        notifyDataSetChanged()

    }

    fun getProductInt(position: Int): ListDataEntity {
        return list[position]
    }

}