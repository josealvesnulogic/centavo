package com.centavo.app.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.centavo.app.ui.screens.ExpenseFormScreen
import com.centavo.app.ui.screens.HomeScreen

@Composable
fun CentavoApp() {
    val nav = rememberNavController()
    val vm: ExpensesViewModel = viewModel()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(
                stateFlow = vm.state,
                onPrev = { vm.goPrevMonth() },
                onNext = { vm.goNextMonth() },
                onTogglePaid = vm::togglePaid,
                onAdd = { nav.navigate("form") }
            )
        }
        composable("form") {
            ExpenseFormScreen(
                onCancel = { nav.popBackStack() },
                onSave = { exp ->
                    vm.upsert(exp)
                    nav.popBackStack()
                }
            )
        }
    }
}