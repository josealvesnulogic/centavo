package com.centavo.app.data

import com.centavo.app.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.YearMonth

interface ExpensesRepository {
    fun expensesFor(month: YearMonth): Flow<List<Expense>>
    suspend fun upsert(expense: Expense)
    suspend fun togglePaid(id: String)
}

class InMemoryExpensesRepository : ExpensesRepository {
    private val state = MutableStateFlow<List<Expense>>(emptyList())

    override fun expensesFor(month: YearMonth): Flow<List<Expense>> =
        state.map { list -> list.filter { YearMonth.from(it.dueDate) == month } }

    override suspend fun upsert(expense: Expense) {
        state.value = state.value.toMutableList().apply {
            val idx = indexOfFirst { it.id == expense.id }
            if (idx >= 0) set(idx, expense) else add(expense)
        }
    }

    override suspend fun togglePaid(id: String) {
        state.value = state.value.map { if (it.id == id) it.copy(isPaid = !it.isPaid) else it }
    }
}