package com.centavo.app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
// Remova a importação individual de SegmentedButton se você a adicionou manualmente
// import androidx.compose.material3.SegmentedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
// Corrija a importação duplicada de KeyboardOptions
// import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.centavo.app.model.Expense
import com.centavo.app.model.ExpenseType
import com.centavo.app.util.Brl
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import androidx.compose.material3.SegmentedButtonDefaults // Mantenha esta importação
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormScreen(
    onCancel: () -> Unit,
    onSave: (Expense) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    val today = LocalDate.now()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    var selectedTypeIndex by remember { mutableStateOf(ExpenseType.ONE_TIME.ordinal) } // Rastrear o índice selecionado
    val expenseTypes = ExpenseType.values() // Array de todos os tipos de despesa

    var totalInstallments by remember { mutableStateOf("6") }
    var currentInstallment by remember { mutableStateOf("1") }
    var category by remember { mutableStateOf("Geral") }
    var note by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Adicionar despesa") }) }
    ) { inner ->
        Column(Modifier.padding(inner).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it.filter { c -> c.isDigit() || c == ',' || c == '.' } },
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                suffix = {
                    val parsed = amountText.replace(".", "").replace(",", ".").toBigDecimalOrNull()
                    if (parsed != null) Text(Brl.format(parsed))
                }
            )

            Text("Vencimento", style = MaterialTheme.typography.labelLarge)
            DatePicker(state = datePickerState)

            // Tipo
            Text("Tipo", style = MaterialTheme.typography.labelLarge)
            // Use o SingleChoiceSegmentedButtonRow para seleção única
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                expenseTypes.forEachIndexed { index, expenseType ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = expenseTypes.size),
                        onClick = { selectedTypeIndex = index },
                        selected = index == selectedTypeIndex
                    ) {
                        Text(expenseType.name.replace("_", " ").lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }) // Formata o nome para exibição
                    }
                }
            }

            val currentExpenseType = expenseTypes[selectedTypeIndex] // Obter o tipo de despesa atual

            if (currentExpenseType == ExpenseType.INSTALLMENT) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = currentInstallment, onValueChange = { currentInstallment = it.filter(Char::isDigit) },
                        label = { Text("Parcela") }, modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = totalInstallments, onValueChange = { totalInstallments = it.filter(Char::isDigit) },
                        label = { Text("Total") }, modifier = Modifier.weight(1f)
                    )
                }
            }

            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Observação (opcional)") }, modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lembrar antes do vencimento")
                Switch(checked = reminder, onCheckedChange = { reminder = it })
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                val canSave = title.isNotBlank() && amountText.isNotBlank() && datePickerState.selectedDateMillis != null
                Button(
                    onClick = {
                        val amount = amountText.replace(".", "").replace(",", ".").toBigDecimal()
                        val date = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                            ZoneId.systemDefault()
                        ).toLocalDate()
                        val expense = Expense(
                            title = title.trim(),
                            amount = amount,
                            dueDate = date,
                            type = currentExpenseType, // Usar o tipo de despesa selecionado
                            totalInstallments = if (currentExpenseType == ExpenseType.INSTALLMENT) totalInstallments.toIntOrNull() else null,
                            currentInstallment = if (currentExpenseType == ExpenseType.INSTALLMENT) currentInstallment.toIntOrNull() else null,
                            category = category.takeIf { it.isNotBlank() },
                            note = note.takeIf { it.isNotBlank() }
                        )
                        onSave(expense)
                        // TODO: se reminder == true, agendar notificação (WorkManager)
                    },
                    enabled = canSave,
                    modifier = Modifier.weight(1f)
                ) { Text("Salvar") }
            }
        }
    }
}
