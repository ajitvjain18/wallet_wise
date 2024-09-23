package com.techmania.expensemanager.Views

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.techmania.expensemanager.Adapters.TransactionAdapter
import com.techmania.expensemanager.Entity.TransactionApplication
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.R
import com.techmania.expensemanager.TransactionViewModel
import com.techmania.expensemanager.TransactionViewModelFactory
import com.techmania.expensemanager.databinding.ActivityMainBinding
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var currentDate = Date()
    var currentMonth = Date()
    val rupeeSymbol = "\u20B9"
    var currentYear = Date()
    private var s = ""
    private var e = ""
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val yearFormat = SimpleDateFormat("YYYY", Locale.getDefault())
    private lateinit var viewModel: TransactionViewModel
    lateinit var adapter: TransactionAdapter
    private var TAB_SELECTED = "Daily"
    private var list: List<TransactionEntity> = emptyList()
    private val createFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let {
                createFile(it)
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"

        val viewModelFactory =
            TransactionViewModelFactory((application as TransactionApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]

        updateDateText()
        initObservers()
        initClicks()


        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteFromDatabase(adapter.getTransaction(viewHolder.adapterPosition))
            }

        }).attachToRecyclerView(binding.recyclerView)

    }

    private fun initObservers() {
        adapter = TransactionAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.getAllTransactionsByDate.observe(this) {
            list = it
            adapter.updateList(it)
            if (it.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.emptyState.visibility = View.GONE
            }
            var totalIncome = 0.00
            var totalExpense = 0.00
            val total: Double

            it.forEach {
                if (it.amount.isNotEmpty()) { // Check if amount is not empty
                    if (it.type.equals("Income")) {
                        totalIncome += it.amount.toDouble()
                    } else {
                        totalExpense += it.amount.toDouble()
                    }
                }
            }
            total = totalIncome - totalExpense
            binding.total.setText(total.toString() + rupeeSymbol)
            binding.totalIncome.setText(totalIncome.toString() + rupeeSymbol)
            binding.totalExpense.setText(totalExpense.toString() + rupeeSymbol)
        }

    }


    private fun updateDateText() {
        val formattedDate = dateFormat.format(currentDate)
        binding.date.text = formattedDate
        viewModel.getTransactionsByDate(formattedDate)
    }

    private fun updateMonthText() {
        val formattedMonth = monthFormat.format(currentMonth)
        binding.date.text = formattedMonth

        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(formattedMonth) ?: Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val lastDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        val fdate = firstDate.replace("1970", "2024")
        val tdate = lastDate.replace("1970", "2024")
        s = fdate
        e = tdate
        filterBetweenDates()

    }

    private fun filterBetweenDates() {
        viewModel.allData.observe(this) {

            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val start = LocalDate.parse(s, dateFormatter)
            val end = LocalDate.parse(e, dateFormatter)

            val subList = it.filter { LocalDate.parse(it.date, dateFormatter) in start..end }
            adapter.updateList(subList)
            if (it.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.emptyState.visibility = View.GONE
            }
            var totalIncome = 0.00
            var totalExpense = 0.00
            var total = 0.00

            it.forEach {
                if (it.amount.isNotEmpty()) { // Check if amount is not empty
                    total += it.amount.toDouble()
                    if (it.type.equals("Income")) {
                        totalIncome += it.amount.toDouble()
                    } else {
                        totalExpense += it.amount.toDouble()
                    }
                }
            }

            binding.total.setText(total.toString())
            binding.totalIncome.setText(totalIncome.toString())
            binding.totalExpense.setText(totalExpense.toString())
        }
    }

    private fun updateYearText() {
        val formattedYear = yearFormat.format(currentYear)
        binding.date.text = formattedYear


        val firstDayOfYear =
            SimpleDateFormat("01-01-yyyy", Locale.getDefault()).parse("01-01-$formattedYear")
                ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = firstDayOfYear

        val firstDateOfYear =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        val lastDateOfYear =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

        s = firstDateOfYear
        e = lastDateOfYear
        filterBetweenDates()

    }


    private fun initClicks() {
        binding.addTransaction.setOnClickListener {
            val fragment = AddTransactionFragment()
            fragment.show(supportFragmentManager, fragment.tag)
        }

        binding.previousDate.setOnClickListener {
            if (TAB_SELECTED.equals("Daily")) {
                currentDate = getPreviousDate(currentDate)
                updateDateText()
            } else if (TAB_SELECTED.equals("Monthly")) {
                currentMonth = moveMonth(currentMonth, -1)
                updateMonthText()
            } else if (TAB_SELECTED.equals("Yearly")) {
                currentYear = moveYear(currentYear, -1)
                updateYearText()
            }
        }

        binding.nextDate.setOnClickListener {
            if (TAB_SELECTED.equals("Daily")) {
                currentDate = getNextDate(currentDate)
                updateDateText()
            } else if (TAB_SELECTED.equals("Monthly")) {
                currentMonth = moveMonth(currentMonth, 1)
                updateMonthText()
            } else if (TAB_SELECTED.equals("Yearly")) {
                currentYear = moveYear(currentYear, 1)
                updateYearText()
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.stats -> {

                    val activity = StatsActivity()
                    val intent = Intent(this@MainActivity, activity::class.java)
                    startActivity(intent)

                    binding.framelayout.visibility = View.VISIBLE
                    binding.dateLayout.visibility = View.GONE
                    binding.tabLayout.visibility = View.GONE
                    supportActionBar?.title = "Stats"
                    binding.incomeExpenseTotalLayout.visibility = View.GONE
                    binding.addTransaction.isVisible = false
                    true
                }

                R.id.accounts -> {
                    val fragment = SummaryFragment()
                    fragment.viewModel = viewModel
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.framelayout, fragment)
                    transaction.commit()
                    binding.framelayout.visibility = View.VISIBLE
                    binding.dateLayout.visibility = View.GONE
                    binding.tabLayout.visibility = View.GONE
                    binding.incomeExpenseTotalLayout.visibility = View.VISIBLE
                    binding.addTransaction.isVisible = false
                    supportActionBar?.title = "Accounts"
                    true
                }

                R.id.transactions -> {
                    binding.framelayout.visibility = View.GONE
                    binding.dateLayout.visibility = View.VISIBLE
                    binding.tabLayout.visibility = View.VISIBLE
                    binding.incomeExpenseTotalLayout.visibility = View.VISIBLE
                    binding.addTransaction.isVisible = true
                    supportActionBar?.title = "Transactions"
                    true
                }

                R.id.more -> {
                    val fragment = supportFragmentManager.findFragmentById(binding.framelayout.id)
                    if (fragment is MoreFragment) {
                        supportFragmentManager.popBackStack()
                    } else {
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.setCustomAnimations(
                            R.anim.anim_slide_start_from_left,
                            R.anim.anim_slide_end_to_left,
                            R.anim.anim_slide_start_from_left,
                            R.anim.anim_slide_end_to_left
                        )
                        transaction.replace(binding.framelayout.id, MoreFragment())
                        binding.framelayout.visibility = View.VISIBLE
                        binding.addTransaction.isVisible = false
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                    true
                }

                else -> false
            }
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    TAB_SELECTED = it.text.toString()

                    if (TAB_SELECTED.equals("Daily")) {
                        updateDateText()
                    }
                    if (TAB_SELECTED.equals("Monthly")) {
                        updateMonthText()
                    }
                    if (TAB_SELECTED.equals("Yearly")) {
                        updateYearText()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })

    }

    private fun moveYear(date: Date, i: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.YEAR, i)
        return calendar.time
    }

    fun moveMonth(date: Date, months: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, months)
        return calendar.time
    }

    private fun getNextDate(currentDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    private fun getPreviousDate(currentDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                showDeleteDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete all transactions")
        dialogMessage.setMessage("Are you sure you want to delete all transactions? This action cannot be undone.")
        dialogMessage.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
        dialogMessage.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->
                viewModel.deleteAll()
            })
        dialogMessage.create().show()
    }

    fun exportToCsv() {
        val wb: WritableWorkbook
        val filePath = Environment.getExternalStorageDirectory().toString() + "/transactions.xls"
        val file = File(filePath)
        try {
            file.createNewFile()
            val os = FileOutputStream(file)
            wb = Workbook.createWorkbook(os)
            val sheet: WritableSheet = wb.createSheet("Transactions", 0)

            val headers =
                arrayOf("Transaction ID", "Type", "Category", "Account", "Note", "Date", "Amount")
            for (i in headers.indices) {
                val label = Label(i, 0, headers[i])
                sheet.addCell(label)
            }

            // Write data
            var row = 1
            for (transaction in list) {
                sheet.addCell(Label(0, row, transaction.transactionID.toString()))
                sheet.addCell(Label(1, row, transaction.type))
                sheet.addCell(Label(2, row, transaction.category))
                sheet.addCell(Label(3, row, transaction.account))
                sheet.addCell(Label(4, row, transaction.note))
//                sheet.addCell(Label(5, row, transaction.date))
                sheet.addCell(Label(6, row, transaction.amount))
                row++
            }

            wb.write()
            wb.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createFile() {

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/vnd.ms-excel"
        intent.putExtra(Intent.EXTRA_TITLE, "transactions.xls")
        createFile.launch(intent)
    }

    fun createFile(uri: Uri) {

        try {
            val workbook = HSSFWorkbook()
            val sheet = workbook.createSheet("Transaction")
            val headerRow = sheet.createRow(0)
            val headers =
                arrayOf("Transaction ID", "Type", "Category", "Account", "Note", "Date", "Amount")
            headers.forEachIndexed { index, header ->
                headerRow.createCell(index).setCellValue(header)
            }
            list.forEachIndexed { rowIndex, transactionEntity ->
                val row = sheet.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(transactionEntity.transactionID.toString())
                row.createCell(1).setCellValue(transactionEntity.type)
                row.createCell(2).setCellValue(transactionEntity.category.toString())
                row.createCell(3).setCellValue(transactionEntity.account.toString())
                row.createCell(4).setCellValue(transactionEntity.note.toString())
                row.createCell(5).setCellValue(transactionEntity.date.toString())
                row.createCell(6).setCellValue(transactionEntity.amount.toString())
            }
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                workbook.write(outputStream)
            }
            Toast.makeText(this, "File exported successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}