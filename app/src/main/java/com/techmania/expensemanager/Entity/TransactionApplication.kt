package com.techmania.expensemanager.Entity

import android.app.Application

class TransactionApplication : Application() {
    val database by lazy { TransactionDatabase.getdatabase(this) }
    val repository by lazy { TransactionRepository(database.getTransactionDao()) }
}