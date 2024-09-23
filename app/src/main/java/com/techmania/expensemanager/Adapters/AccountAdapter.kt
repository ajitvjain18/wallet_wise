package com.techmania.expensemanager.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmania.expensemanager.Models.Account
import com.techmania.expensemanager.databinding.RowAccountBinding

class AccountAdapter(var context: Context, val list: ArrayList<Account>) :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    var onItemCLick : ((Account)-> Unit)? = null

    inner class AccountViewHolder(val binding: RowAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding: RowAccountBinding = RowAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account:Account = list[position]
        holder.binding.accountName.setText(account.accountName)


        holder.itemView.setOnClickListener {
            onItemCLick?.invoke(list[position])
        }
    }

}