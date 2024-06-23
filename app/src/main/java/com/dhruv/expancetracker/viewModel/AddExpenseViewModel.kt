package com.dhruv.expancetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhruv.expancetracker.data.ExpenseDataBase
import com.dhruv.expancetracker.data.dao.ExpenseDao
import com.dhruv.expancetracker.data.model.ExpenseEntity

class AddExpenseViewModel(
    val dao: ExpenseDao
) : ViewModel() {
    suspend fun addExpense(expenseEntity: ExpenseEntity) :Boolean{
        return try {
            dao.insertExpense(expenseEntity)
            true
        }catch (e:Throwable){
            return false
        }
    }
}

class AddExpenseViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}