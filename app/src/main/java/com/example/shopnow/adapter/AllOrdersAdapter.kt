package com.example.shopnow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.databinding.AllOrdersDetailsItemLayoutBinding
import com.example.shopnow.model.AllOrdersModel

class AllOrdersAdapter(val context: Context, private val list : ArrayList<AllOrdersModel>) : RecyclerView.Adapter<AllOrdersAdapter.AllOrdersViewHolder>() {

    inner class AllOrdersViewHolder(val binding: AllOrdersDetailsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder {
        val binding  = AllOrdersDetailsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllOrdersViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AllOrdersViewHolder, position: Int) {
        holder.binding.productNameTV.text = list[position].name
        holder.binding.productPriceTV.text = list[position].price

        when (list[position].status) {
            "Ordered" -> {
                holder.binding.productStatus.text = "Ordered"
            }
            "Dispatched" -> {
                holder.binding.productStatus.text = "Dispatched"
            }
            "Delivered" -> {
                holder.binding.productStatus.text = "Delivered"
            }
            "Canceled" -> {
                holder.binding.productStatus.text = "Canceled"
            }
        }
    }
}