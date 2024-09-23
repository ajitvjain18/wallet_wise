package com.techmania.expensemanager.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmania.expensemanager.databinding.LayoutRowTransactionBinding
import com.techmania.expensemanager.databinding.LayoutTipsItemBinding

class TipsAdapter(
    private val context: Context,
    private val tipTitle: List<String>,
    private val tipDes: List<String>
) : RecyclerView.Adapter<TipsAdapter.TipsViewHolder>() {

    inner class TipsViewHolder(val binding: LayoutTipsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, des: String) {
            binding.title.text = title
            binding.description.text = des
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding: LayoutTipsItemBinding = LayoutTipsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TipsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tipTitle.size
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        val tip = tipTitle[position]
        val des = tipDes[position]
        holder.bind(tip, des)
    }
}