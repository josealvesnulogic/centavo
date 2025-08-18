package com.centavo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.centavo.app.model.Expense
import com.centavo.app.ui.HomeUiState
import com.centavo.app.ui.theme.Danger
import com.centavo.app.ui.theme.Success
import com.centavo.app.util.Brl
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    stateFlow: StateFlow<HomeUiState>,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onTogglePaid: (String) -> Unit,
    onAdd: () -> Unit,
) {
    val state by stateFlow.collectAsState()
    val monthTitle = state.month.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("pt", "BR")) else it.toString() } +
            "/${state.month.year}"

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Minhas Despesas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Icon(Icons.Default.Add, contentDescription = "Adicionar") }
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Saldo a pagar", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Text(Brl.format(state.totalToPay), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            // Mês com setas
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onPrev) { Icon(Icons.Default.ArrowBack, contentDescription = "Anterior") }
                Text(monthTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                IconButton(onClick = onNext) { Icon(Icons.Default.ArrowForward, contentDescription = "Próximo") }
            }

            if (state.expenses.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma despesa neste mês. Toque em + para adicionar.")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.expenses, key = { it.id }) { e ->
                        ExpenseRow(expense = e, onTogglePaid = { onTogglePaid(e.id) })
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, onTogglePaid: () -> Unit) {
    val isOverdue = !expense.isPaid && expense.dueDate.isBefore(LocalDate.now())
    val bg: Color? = when {
        expense.isPaid -> Success
        isOverdue -> Danger
        else -> null
    }
    val dateFmt = DateTimeFormatter.ofPattern("d/M/yyyy")
    Column(
        Modifier
            .fillMaxWidth()
            .then(if (bg != null) Modifier.background(bg.copy(alpha = 0.4f)) else Modifier)
            .clickable { onTogglePaid() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(expense.title, style = MaterialTheme.typography.titleSmall)
            Text(Brl.format(expense.amount), fontWeight = FontWeight.SemiBold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val subtitle = buildString {
                append(expense.dueDate.format(dateFmt))
                when {
                    expense.isPaid -> append("  •  pago")
                    isOverdue -> append("  •  atrasada")
                }
                if (expense.totalInstallments != null && expense.currentInstallment != null) {
                    append("  •  ${expense.currentInstallment}/${expense.totalInstallments}")
                }
            }
            Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}