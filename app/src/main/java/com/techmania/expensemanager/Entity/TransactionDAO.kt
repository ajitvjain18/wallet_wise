package com.techmania.expensemanager.Entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDAO {

    @Query("SELECT * from `transaction` ORDER BY transactionID ASC")
    fun getAllRecords() : LiveData<List<TransactionEntity>>

    @Insert
    fun insert(transaction: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM `transaction` WHERE date=:date ORDER BY transactionID ASC")
    fun getRecordsByDate(date:String) : List<TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :from AND :to ORDER BY transactionID ASC")
    fun getRecordsByMonth(from: String,to:String): List<TransactionEntity>

    @Query("DELETE from `transaction`")
    fun deleteAllTransactions()

}