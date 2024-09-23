package com.techmania.expensemanager.Views

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.techmania.expensemanager.Adapters.AccountAdapter
import com.techmania.expensemanager.Adapters.CategoryAdapter
import com.techmania.expensemanager.Entity.TransactionDatabase
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.Models.Account
import com.techmania.expensemanager.Models.Category
import com.techmania.expensemanager.R
import com.techmania.expensemanager.TransactionViewModel
import com.techmania.expensemanager.databinding.FragmentAddTransactionBinding
import com.techmania.expensemanager.databinding.ListDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTransactionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    private val calendar = Calendar.getInstance()
    var alertDialog: AlertDialog? = null
    private lateinit var viewModel: TransactionViewModel
    var type = ""
    var incomeSelected = false
    var expenseSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        initClicks()
        return binding.root
    }

    private fun initClicks() {

        binding.incomeButton.setOnClickListener {
            type = "Income"
            incomeSelected = true
            expenseSelected = false
            it.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.income_selector
            )
            binding.expenseButton.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.default_selector
            )
            binding.incomeButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white_app
                )
            )
            binding.expenseButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_color
                )
            )

        }
        binding.expenseButton.setOnClickListener {
            type = "Expense"
            incomeSelected = false
            expenseSelected = true
            it.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.expense_selector
            )
            binding.incomeButton.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.default_selector
            )
            binding.expenseButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white_app
                )
            )
            binding.incomeButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_color
                )
            )
        }

        binding.selectDate.setOnClickListener {
            showDatePicker()
        }

        binding.categroy.setOnClickListener {
            val dialogBinding: ListDialogBinding = ListDialogBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogBinding.root)

            val expenseCategory = ArrayList<Category>()
            expenseCategory.add(Category("Clothing", R.drawable.clothing_svgrepo_com, R.color.cat1))
            expenseCategory.add(Category("Fuel", R.drawable.gas_station_svgrepo_com, R.color.cat2))
            expenseCategory.add(Category("Grocery", R.drawable.grocery_candy_svgrepo_com, R.color.cat3))
            expenseCategory.add(Category("Health", R.drawable.ic_health, R.color.cat4))
            expenseCategory.add(Category("Rent", R.drawable.ic_house, R.color.cat5))
            expenseCategory.add(Category("Insurance", R.drawable.insurance_money_protection_svgrepo_com, R.color.cat6))
            expenseCategory.add(Category("Internet", R.drawable.internet_speed_meter_lite_svgrepo_com, R.color.cat7))
            expenseCategory.add(Category("Transport", R.drawable.transport, R.color.cat8))
            expenseCategory.add(Category("Travel", R.drawable.travel_bag_svgrepo_com, R.color.cat9))

            val incomeCategory = ArrayList<Category>()
            incomeCategory.add(Category("Allowance", R.drawable.allowance, R.color.cat1))
            incomeCategory.add(Category("Salary", R.drawable.salary, R.color.cat1))
            incomeCategory.add(Category("Petty cash", R.drawable.pettycash, R.color.cat1))
            incomeCategory.add(Category("Bonus", R.drawable.bonus, R.color.cat1))
            incomeCategory.add(Category("Other", R.drawable.other_insurance_svgrepo_com, R.color.cat1))
            incomeCategory.add(Category("Trading", R.drawable.blackboard_graph_svgrepo_com, R.color.cat1))

            if (type.equals("Income")){
                val adapter = CategoryAdapter(requireContext(), incomeCategory)
                dialogBinding.rv.adapter = adapter
                adapter.onItemCLick = {
                    binding.categroy.setText(it.categoryName)
                    alertDialog?.dismiss()

                }
                alertDialog = builder.show()
            }else{
                val adapter = CategoryAdapter(requireContext(), expenseCategory)
                dialogBinding.rv.adapter = adapter
                adapter.onItemCLick = {
                    binding.categroy.setText(it.categoryName)
                    alertDialog?.dismiss()

                }
                alertDialog = builder.show()
            }

        }

        binding.account.setOnClickListener {
            val dialogBinding: ListDialogBinding = ListDialogBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogBinding.root)
            val list = ArrayList<Account>()

            list.add(Account("Cash", "0"))
            list.add(Account("Bank Accounts", "0"))
            list.add(Account("UPI", "0"))
            list.add(Account("Other", "0"))

            val adapter = AccountAdapter(requireContext(), list)
            dialogBinding.rv.adapter = adapter
            dialogBinding.rv.layoutManager = LinearLayoutManager(requireContext())
            dialogBinding.rv.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            adapter.onItemCLick = {
                binding.account.setText(it.accountName)
                alertDialog?.dismiss()

            }
            alertDialog = builder.show()
        }

        binding.saveButton.setOnClickListener {
            if (isFormValid()) {
                val amount = binding.amount.text.toString()
                val category = binding.categroy.text.toString()
                val account = binding.account.text.toString()
                val note = binding.note.text.toString()
                val date = binding.selectDate.text.toString()


                val transaction = TransactionEntity(type, category, account, note, date, amount)
                viewModel.addToDatabase(transaction)
                val td = TransactionDatabase.getdatabase(requireContext())
                td.backupDatabase(requireContext())
                dismiss()
            }
            else {
                showSnackBar("Please enter required details")
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }

    fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val date = selectedDate.time

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                val dateToDisplay = formattedDate.replace("/","-")

                binding.selectDate.setText(dateToDisplay)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun removeError(vararg tls: TextInputLayout) {
        for (et in tls)
            et.error = null
    }


    fun isFormValid() : Boolean{
        removeError(binding.tlSelectDate,binding.tlAmount,binding.tlCateogry,binding.tlAccount)

        if (binding.selectDate.text.toString().isEmpty()){
            binding.tlSelectDate.error = "Mandatory"
            return false
        }
        if (binding.amount.text.toString().isEmpty()){
            binding.tlAmount.error = "Mandatory"
            return false
        }
        if (binding.categroy.text.toString().isEmpty()) {
            binding.tlCateogry.error = "Mandatory"
            return false
        }
        if (binding.account.text.toString().isEmpty()) {
            binding.tlAccount.error = "Mandatory"
            return false
        }
        if (!incomeSelected && !expenseSelected) {
            showSnackBar("Select Transaction Type")
            return false
        }
        return true
    }

    fun showSnackBar(message: String, color: Int? = R.color.el_error_text_color) {
        view?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
            color?.let { c ->
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(ContextCompat.getColor(requireContext(),c))
                snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_el))
            }
            snackBar.show()
        }
    }
}