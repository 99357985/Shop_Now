package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.databinding.LayoutProductListItemBinding
import com.example.shopnow.model.AddProductModel

class ProductAdapter(val context: Context, val list : ArrayList<AddProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: LayoutProductListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductListItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ProductViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.productListItemCoverImg)

        holder.binding.apply {
            productListItemProductName.text = data.productName
            productListItemCategoryName.text = data.productCategory
            productListItemMRP.text = data.productMrp
            productListItemBtnSP.text = data.productSp
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }
}