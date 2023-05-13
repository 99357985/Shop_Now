package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.databinding.LayoutCartListItemBinding
import com.example.shopnow.db.Product
import com.example.shopnow.db.ProductDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context,val list: List<Product>) : RecyclerView.Adapter<CartAdapter.CartViewModel>() {

    inner class CartViewModel(val binding: LayoutCartListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewModel {
        val binding = LayoutCartListItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return CartViewModel(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CartViewModel, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView2)

        holder.binding.apply {
            textView.text = list[position].productName
            textView5.text = list[position].productSp
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        val dao = ProductDatabase.getInstance(context).getProductDao()
        holder.binding.imageViewDelete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(Product(list[position].productId, list[position].productName, list[position].productImage, list[position].productSp))
            }
        }
    }
}