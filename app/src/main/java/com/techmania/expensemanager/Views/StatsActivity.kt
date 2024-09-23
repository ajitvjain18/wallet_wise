package com.techmania.expensemanager.Views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.google.android.material.tabs.TabLayout
import com.techmania.expensemanager.Entity.TransactionApplication
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.TransactionViewModel
import com.techmania.expensemanager.TransactionViewModelFactory
import com.techmania.expensemanager.databinding.ActivityStatsBinding


class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding
    private var TAB_SELECTED = ""
    lateinit var viewModel: TransactionViewModel
    private var list: List<TransactionEntity> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        val viewModelFactory =
            TransactionViewModelFactory((application as TransactionApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Stats"

        initObservers()
        handleClicks()
    }

    private fun initObservers() {
        viewModel.statsData.observe(this) {
            if (it.isNotEmpty()) {
                binding.emptyState.visibility = View.GONE
                list = it
            } else {
                binding.emptyState.visibility = View.VISIBLE
            }
        }
    }

    private fun handleClicks() {

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    TAB_SELECTED = it.text.toString()

                    if (TAB_SELECTED.equals("INCOME")) {
                        handleChart(true)
                    }

                    if (TAB_SELECTED.equals("EXPENSE")) {
                        handleChart(false)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }

    private fun handleChart(isIncome: Boolean) {

        val pie = AnyChart.pie()
        val categoryMap = mutableMapOf<String, Double>()

        val filteredData = if (isIncome) {
            list.filter {
                it.type == "Income"
            }
        } else {
            list.filter {
                it.type == "Expense"
            }
        }

        for (transaction in filteredData) {
            val category = transaction.category
            val amount = transaction.amount.toDouble()
            categoryMap[category] = categoryMap.getOrDefault(category, 0.0) + amount
        }

        val pieData = mutableListOf<DataEntry>()
        for ((category, amount) in categoryMap) {
            pieData.add(ValueDataEntry(category, amount))
        }

        pie.labels().position("outside")

        pie.legend().title().enabled(true)
        pie.legend().title()
            .text(if (isIncome) "Income Transactions" else "Expense Transactions")
            .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)

        pie.data(pieData)

        binding.chart.setChart(pie)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}