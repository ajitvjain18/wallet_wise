package com.techmania.expensemanager.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techmania.expensemanager.Models.Category
import com.techmania.expensemanager.databinding.SampleCategoryItemBinding

class CategoryAdapter(var context: Context, val list: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

   var onItemCLick : ((Category)-> Unit)? = null

    inner class CategoryViewHolder(val binding: SampleCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: SampleCategoryItemBinding =
            SampleCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.category.text = list[position].categoryName
        holder.binding.icon.setImageResource(list[position].categoryImage)


        holder.itemView.setOnClickListener {
            onItemCLick?.invoke(list[position])
        }


    }

}