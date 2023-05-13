package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.CategoryActivity
import com.example.shopnow.databinding.LayoutCategoryListItemBinding
import com.example.shopnow.model.CategoryModel

class CategoryAdapter(var context : Context, var list: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_list_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.categoryListItemTextView.text = list[position].cat
        Glide.with(context).load(list[position].img).into(holder.binding.categoryListItemImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("cat", list[position].cat)
            context.startActivity(intent)
        }
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutCategoryListItemBinding.bind(view)
    }

}