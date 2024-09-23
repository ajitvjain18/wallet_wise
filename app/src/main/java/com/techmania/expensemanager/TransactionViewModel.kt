package com.techmania.expensemanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.techmania.expensemanager.Entity.TransactionDatabase
import com.techmania.expensemanager.Entity.TransactionEntity
import com.techmania.expensemanager.Entity.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private var sDate: String = ""


    val allData: LiveData<List<TransactionEntity>> = repository.allTransactionRecords

    val statsData : LiveData<List<TransactionEntity>> = repository.allTransactionRecords


    private val _getAllTransactionsByDate: MutableLiveData<List<TransactionEntity>> = MutableLiveData()
    val getAllTransactionsByDate: LiveData<List<TransactionEntity>> = _getAllTransactionsByDate

    fun addToDatabase(transaction: TransactionEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(transaction)
        getTransactionsByDate(sDate)
    }

    fun deleteFromDatabase(transaction: TransactionEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(transaction)
        getTransactionsByDate(sDate)
    }

    fun getTransactionsByDate(date: String) = viewModelScope.launch(Dispatchers.IO) {
        sDate = date
        val data = repository.getRecordsByDate(sDate)
        _getAllTransactionsByDate.postValue(data)
    }


    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        getTransactionsByDate(sDate)
    }
}


class TransactionViewModelFactory(private var repository: TransactionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown viewmodel")
        }
    }

}