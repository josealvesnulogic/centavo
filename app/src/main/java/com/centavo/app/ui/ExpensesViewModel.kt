package com.centavo.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.centavo.app.data.ExpensesRepository
import com.centavo.app.data.InMemoryExpensesRepository
import com.centavo.app.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth

data class HomeUiState(
    val month: YearMonth = YearMonth.now(),
    val expenses: List<Expense> = emptyList(),
    val totalToPay: BigDecimal = BigDecimal.ZERO,
)

class ExpensesViewModel(
    private val repo: ExpensesRepository = InMemoryExpensesRepository()
) : ViewModel() {

    private val month = MutableStateFlow(YearMonth.now())

    private val expensesForMonth = month.flatMapLatest { m -> repo.expensesFor(m) }

    val state = combine(month, expensesForMonth) { m, list ->
        val total = list.filter { !it.isPaid }.fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }
        HomeUiState(month = m, expenses = list.sortedBy { it.dueDate }, totalToPay = total)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun goPrevMonth() { month.value = month.value.minusMonths(1) }
    fun goNextMonth() { month.value = month.value.plusMonths(1) }

    fun upsert(expense: Expense) = viewModelScope.launch { repo.upsert(expense) }
    fun togglePaid(id: String) = viewModelScope.launch { repo.togglePaid(id) }
}