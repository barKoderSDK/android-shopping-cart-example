package com.example.barkodershopapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import com.barkoder.shoppingApp.net.databinding.PricehistoryItemBinding
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PriceHistoryAdapter(private var list: ArrayList<PriceHistory>) :
    RecyclerView.Adapter<PriceHistoryAdapter.ViewHolder>() {

    fun setPricesList(pricesList: ArrayList<PriceHistory>) {
        this.list = pricesList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: PricehistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: PriceHistory) {
            binding.textPriceHistory.setText(list.lastPrice)
            binding.textPriceDate.setText(list.dataChangedPrice)

            binding.imageView.load(list.imageArrow)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PriceHistoryAdapter.ViewHolder {
        val binding =
            PricehistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


}