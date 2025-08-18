package com.centavo.app.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

enum class ExpenseType { FIXED, INSTALLMENT, ONE_TIME }

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: BigDecimal,
    val dueDate: LocalDate,
    val type: ExpenseType,
    val totalInstallments: Int? = null, // para parceladas
    val currentInstallment: Int? = null,
    val category: String? = null,
    val isPaid: Boolean = false,
    val note: String? = null,
)