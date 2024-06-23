package com.dhruv.expancetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dhruv.expancetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query(value = "SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Query(value = "DELETE FROM expense_table WHERE id = :id")
    suspend fun deleteExpense(id: Int)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)


}