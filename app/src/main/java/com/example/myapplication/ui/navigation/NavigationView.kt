package com.example.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.data.models.CreditCardTypes
import com.example.myapplication.ui.elements.QRScannerComp
import com.example.myapplication.ui.screens.CreditCardForm
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.TransactionForm
import com.example.myapplication.ui.viewmodels.CreditCardViewModel
import com.example.myapplication.ui.viewmodels.GraphsViewModel
import com.example.myapplication.ui.viewmodels.MainScreenViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun NavigationView(navController: NavHostController) {
    val mainScreenViewModel = hiltViewModel<MainScreenViewModel>()
    val graphScreenViewModel = hiltViewModel<GraphsViewModel>()

    NavHost(
        navController = navController,
        startDestination = MAIN_SCREEN_ROUTE,
        route = ROOT_ROUTE
    ) {
        spendingGraphNavGraph(navController, graphScreenViewModel)
        homeNavGraph(navController, mainScreenViewModel)
    }



}



