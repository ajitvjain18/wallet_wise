package com.techmania.expensemanager.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")

class TransactionEntity(
    @ColumnInfo(name = "type") val type: String,

    @ColumnInfo(name = "category") val category: String,

    @ColumnInfo(name = "account") val account: String,

    @ColumnInfo(name = "note") val note: String,

    @ColumnInfo(name = "date") val date: String,

    @ColumnInfo(name = "amount") val amount: String,
) {
    @PrimaryKey(autoGenerate = true)
    var transactionID = 0
}