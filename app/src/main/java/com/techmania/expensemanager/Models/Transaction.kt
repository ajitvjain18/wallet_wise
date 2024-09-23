package com.techmania.expensemanager.Models

import java.util.Date

data class Transaction(val type:String,val category: String,val account:String,val note:String,val date:String,val amount:Double,val id:Long)