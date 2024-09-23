package com.techmania.expensemanager.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techmania.expensemanager.TransactionViewModel
import com.techmania.expensemanager.databinding.LayoutSummaryBinding

class SummaryFragment : Fragment() {
    private lateinit var binding: LayoutSummaryBinding
    lateinit var viewModel: TransactionViewModel
    val rupeeSymbol = "\u20B9"
    private lateinit var adapter: SummaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        adapter = SummaryAdapter()
        binding.summaryRv.adapter = adapter
        viewModel.allData.observe(viewLifecycleOwner){
            adapter.updateList(it)
            var totalIncome = 0.00
            var totalExpense = 0.00
            val totalBalance: Double

            it.forEach {
                if (it.amount.isNotEmpty()){
                    if (it.type.equals("Income")){
                        totalIncome += it.amount.toDouble()
                    } else{
                        totalExpense += it.amount.toDouble()
                    }
                }
            }
            totalBalance = totalIncome - totalExpense
            binding.total.setText(totalBalance.toString()+rupeeSymbol)
            binding.totalIncome.setText(totalIncome.toString()+rupeeSymbol)
            binding.totalExpense.setText(totalExpense.toString()+rupeeSymbol)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSummaryBinding.inflate(inflater,container,false)
        return binding.root
    }
}