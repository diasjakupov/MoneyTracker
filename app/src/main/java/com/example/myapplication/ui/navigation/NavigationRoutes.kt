package com.example.myapplication.ui.navigation

import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.models.CreditCardTypes


const val GRAPH_ROUTE = "graph"
const val MAIN_SCREEN_ROUTE = "main"
const val ROOT_ROUTE = "root"

sealed class NavigationRoutes(val route: String) {
    object MainScreen : NavigationRoutes("main_screen")
    object CreditCardForm : NavigationRoutes("card_form")
    object CreditCardFormUpdate :
        NavigationRoutes("card_form_update")


    object GraphsScreen : NavigationRoutes("graphs_screen")
    object TransactionForm : NavigationRoutes("transaction_form")
    object TransactionFormUpdate : NavigationRoutes("transaction_form_update")

    object QRScanner: NavigationRoutes("qr_scanner")

}
