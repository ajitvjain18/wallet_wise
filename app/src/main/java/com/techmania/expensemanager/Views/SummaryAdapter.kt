package com.techmania.expensemanager.Views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.R
import com.techmania.expensemanager.databinding.LayoutSummaryItemBinding

class SummaryAdapter() : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {
    private var list: List<TransactionEntity> = emptyList()
    val rupeeSymbol = "\u20B9"
    inner class SummaryViewHolder(val binding: LayoutSummaryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding: LayoutSummaryItemBinding =
            LayoutSummaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<TransactionEntity>) {
        this.list = emptyList()
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val transaction : TransactionEntity = list[position]
        val accountName = transaction.account
        when(accountName){
            "Cash" -> holder.binding.ivTransaction.setImageResource(R.drawable.cash_acc)
            "Bank Accounts" -> holder.binding.ivTransaction.setImageResource(R.drawable.bank_acc)
            "UPI" -> holder.binding.ivTransaction.setImageResource(R.drawable.upi_icon)
            "Other" -> holder.binding.ivTransaction.setImageResource(R.drawable.other_acc)
            else->{

            }
        }
        holder.binding.transactionLabel.setText(transaction.account)
        holder.binding.transactionAmount.setText(transaction.amount)
        holder.binding.transactionDate.setText(transaction.date.toString())
        holder.binding.transactionAmount.setTextColor(
            ContextCompat.getColor(holder.binding.root.context,
            if (transaction.type == "Income")
                R.color.green else R.color.red)
        );
    }

}