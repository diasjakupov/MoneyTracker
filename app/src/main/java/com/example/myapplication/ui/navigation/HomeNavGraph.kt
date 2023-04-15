package com.example.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.myapplication.ui.elements.QRScannerComp
import com.example.myapplication.ui.screens.CreditCardForm
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.TransactionForm
import com.example.myapplication.ui.viewmodels.CreditCardViewModel
import com.example.myapplication.ui.viewmodels.MainScreenViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.homeNavGraph(navController: NavController, mainScreenViewModel: MainScreenViewModel){
    navigation(startDestination = NavigationRoutes.MainScreen.route, route = MAIN_SCREEN_ROUTE) {
        composable(NavigationRoutes.MainScreen.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }
            val creditCardViewModel = hiltViewModel<CreditCardViewModel>(parentEntry)
            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            MainScreen(
                navController = navController,
                creditCardViewModel = creditCardViewModel,
                transactionViewModel = transactionsViewModel,
                mainScreenViewModel = mainScreenViewModel
            )
        }
        composable(NavigationRoutes.CreditCardForm.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }
            val creditCardViewModel = hiltViewModel<CreditCardViewModel>(parentEntry)
            CreditCardForm(cardId = -1, viewModel = creditCardViewModel) {
                navController.navigateUp()
                mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
            }
        }

        composable(
            NavigationRoutes.CreditCardFormUpdate.route + "/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.IntType })
        ) {
            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }
            val cardId = it.arguments!!.getInt("cardId")
            val creditCardViewModel = hiltViewModel<CreditCardViewModel>(parentEntry)
            CreditCardForm(
                viewModel = creditCardViewModel,
                cardId = cardId
            ) {
                navController.navigateUp()
                mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
            }
        }
        composable(NavigationRoutes.TransactionForm.route + "/{cardId}", arguments = listOf(
            navArgument("cardId") { type = NavType.IntType }
        )) {
            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }

            val cardId = it.arguments!!.getInt("cardId")

            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)

            TransactionForm(
                viewModel = transactionsViewModel,
                cardId = cardId,
                chequeId = -1,
                onGoBack = {
                    navController.navigateUp()
                    transactionsViewModel.clearTransactionsInfo()
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
                },
                onQRScan = {
                    navController.navigate(NavigationRoutes.QRScanner.route)
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.QRScanner
                }
            )
        }
        composable(NavigationRoutes.TransactionFormUpdate.route + "/{cardId}/{chequeId}",
            arguments = listOf(
                navArgument("cardId") { type = NavType.IntType },
                navArgument("chequeId") { type = NavType.IntType }
            )) {

            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }

            val cardId = it.arguments!!.getInt("cardId")
            val chequeId = it.arguments!!.getInt("chequeId")

            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)

            TransactionForm(
                viewModel = transactionsViewModel,
                onGoBack = {
                    navController.navigateUp()
                    transactionsViewModel.clearTransactionsInfo()
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
                },
                cardId = cardId,
                chequeId = chequeId,
                onQRScan = {
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.QRScanner
                    navController.navigate(NavigationRoutes.QRScanner.route)
                }
            )
        }
        composable(NavigationRoutes.QRScanner.route) {
            val parentEntry = remember {
                navController.getBackStackEntry(MAIN_SCREEN_ROUTE)
            }
            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)

            QRScannerComp(navController, transactionsViewModel)
        }
    }
}