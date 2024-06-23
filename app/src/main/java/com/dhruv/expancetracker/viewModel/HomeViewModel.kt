package com.dhruv.expancetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dhruv.expancetracker.R
import com.dhruv.expancetracker.Utils
import com.dhruv.expancetracker.data.ExpenseDataBase
import com.dhruv.expancetracker.data.dao.ExpenseDao
import com.dhruv.expancetracker.data.model.ExpenseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    dao: ExpenseDao
) : ViewModel() {

    val expenses = dao.getAllExpenses()

    fun getBalance(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            } else {
                total -= it.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalExpenses(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getItemIcon(item : ExpenseEntity) : Int{
        if (item.category == "Paypal"){
            return R.drawable.paypal
        }else if (item.category == "Netflix" ){
            return R.drawable.netflix
        }else if (item.category == "Starbucks"){
            return R.drawable.starbuks
        }
        return R.drawable.upwork

    }
    

}





class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown")
    }
}

