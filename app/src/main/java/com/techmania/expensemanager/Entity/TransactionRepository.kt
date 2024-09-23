package com.techmania.expensemanager.Entity

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TransactionRepository(private val transactionDAO: TransactionDAO) {
    val allTransactionRecords: LiveData<List<TransactionEntity>> = transactionDAO.getAllRecords()

    @WorkerThread
    fun insert(transaction: TransactionEntity) {
        transactionDAO.insert(transaction)
    }

    @WorkerThread
    fun delete(transaction: TransactionEntity) {
        transactionDAO.delete(transaction)
    }

    @WorkerThread
    fun deleteAll() {
        transactionDAO.deleteAllTransactions()
    }

    @WorkerThread
    fun getRecordsByDate(date: String): List<TransactionEntity> {
        return transactionDAO.getRecordsByDate(date)
    }

}