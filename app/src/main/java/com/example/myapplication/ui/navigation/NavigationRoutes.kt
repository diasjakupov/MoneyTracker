package com.example.myapplication.ui.navigation

import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.models.CreditCardTypes

sealed class NavigationRoutes(val route: String) {
    object MainScreen : NavigationRoutes("main_screen")
    object CreditCardForm : NavigationRoutes("card_form")
    object CreditCardFormUpdate :
        NavigationRoutes("card_form_update")


    object GraphsScreen : NavigationRoutes("graphs_screen")
    object TransactionForm : NavigationRoutes("transaction_form")
    object TransactionFormUpdate: NavigationRoutes("transaction_form_update")
}
