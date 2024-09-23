package com.techmania.expensemanager.Adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.Models.Category
import com.techmania.expensemanager.Models.Transaction
import com.techmania.expensemanager.R
import com.techmania.expensemanager.databinding.LayoutRowTransactionBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter() :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var list: List<TransactionEntity> = emptyList()

    inner class TransactionViewHolder(val binding: LayoutRowTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding: LayoutRowTransactionBinding = LayoutRowTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getTransaction(position: Int) : TransactionEntity{
        return list[position]
    }

    fun updateList(list: List<TransactionEntity>) {
        this.list = emptyList()
        this.list = list
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction: TransactionEntity = list[position]
        val categoryName = transaction.category
        when(categoryName) {
            "Clothing" -> holder.binding.ivTransaction.setImageResource(R.drawable.clothing_svgrepo_com)
            "Fuel" -> holder.binding.ivTransaction.setImageResource(R.drawable.gas_station_svgrepo_com)
            "Grocery" -> holder.binding.ivTransaction.setImageResource(R.drawable.grocery_candy_svgrepo_com)
            "Health" -> holder.binding.ivTransaction.setImageResource(R.drawable.ic_health)
            "Rent" -> holder.binding.ivTransaction.setImageResource(R.drawable.ic_house)
            "Insurance" -> holder.binding.ivTransaction.setImageResource(R.drawable.insurance_money_protection_svgrepo_com)
            "Internet" -> holder.binding.ivTransaction.setImageResource(R.drawable.internet_speed_meter_lite_svgrepo_com)
            "Transport" -> holder.binding.ivTransaction.setImageResource(R.drawable.transport)
            "Travel" -> holder.binding.ivTransaction.setImageResource(R.drawable.travel_bag_svgrepo_com)

            "Allowance" -> holder.binding.ivTransaction.setImageResource(R.drawable.allowance)
            "Salary" -> holder.binding.ivTransaction.setImageResource(R.drawable.salary)
            "Petty cash" -> holder.binding.ivTransaction.setImageResource(R.drawable.pettycash)
            "Bonus" -> holder.binding.ivTransaction.setImageResource(R.drawable.bonus)
            "Other" -> holder.binding.ivTransaction.setImageResource(R.drawable.other_insurance_svgrepo_com)
            else -> {
                // Handle the case when the category name doesn't match any of the predefined cases
            }
        }
        holder.binding.accountLabel.setText(transaction.account)
        holder.binding.transactionAmount.setText(transaction.amount)
        holder.binding.transactionDate.setText(transaction.date)
        holder.binding.transactionLabel.setText(transaction.category)
        Log.d("ajit", "initObservers: "+ transaction.type.toString())

        holder.binding.transactionAmount.setTextColor(ContextCompat.getColor(holder.binding.root.context,
            if (transaction.type == "Income")
                R.color.green else R.color.red)
        );
    }

}