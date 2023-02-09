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
import com.example.myapplication.ui.screens.CreditCardForm
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.TransactionForm
import com.example.myapplication.ui.viewmodels.CreditCardViewModel
import com.example.myapplication.ui.viewmodels.MainScreenViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun NavigationView(navController: NavHostController) {
    val mainScreenViewModel = hiltViewModel<MainScreenViewModel>()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MainScreen.route,
        route = "Parent"
    ) {
        composable(NavigationRoutes.MainScreen.route) {
            val parentEntry = remember {
                navController.getBackStackEntry("Parent")
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
                navController.getBackStackEntry("Parent")
            }
            val creditCardViewModel = hiltViewModel<CreditCardViewModel>(parentEntry)
            CreditCardForm(onGoBack = {
                navController.navigateUp()
                mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
            }, onSave = { cardName: String, type: String, cardNumber: String, color: ULong ->
                creditCardViewModel.createOrUpdateCard(
                    cardName,
                    type,
                    cardNumber,
                    color, -1
                )
            })
        }

        composable(
            NavigationRoutes.CreditCardFormUpdate.route + "/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.IntType })
        ) {
            val parentEntry = remember {
                navController.getBackStackEntry("Parent")
            }
            val cardId = it.arguments!!.getInt("cardId")
            val creditCardViewModel = hiltViewModel<CreditCardViewModel>(parentEntry)
            CreditCardForm(
                _cardName = creditCardViewModel.creditCardInfo.value.name,
                _cardColor = Color(creditCardViewModel.creditCardInfo.value.color.toULong()),
                _cardNumber = creditCardViewModel.creditCardInfo.value.cardNumber,
                _cardType = CreditCardTypes.getClass(creditCardViewModel.creditCardInfo.value.type),
                onGoBack = {
                    navController.navigateUp()
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
                },
                onSave = { cardName: String, type: String, cardNumber: String, color: ULong ->
                    creditCardViewModel.createOrUpdateCard(
                        cardName,
                        type,
                        cardNumber,
                        color, cardId
                    )
                }
            )


        }


        composable(NavigationRoutes.TransactionForm.route + "/{cardId}", arguments = listOf(
            navArgument("cardId") { type = NavType.IntType }
        )) {
            val parentEntry = remember {
                navController.getBackStackEntry("Parent")
            }
            val cardId = it.arguments!!.getInt("cardId")
            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            TransactionForm(
                onGoBack = {
                    navController.navigateUp()
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
                },
            ) { names, date, prices ->
                transactionsViewModel.createOrUpdateCheque(names, date, prices, cardId, -1)
            }
        }
        composable(NavigationRoutes.TransactionFormUpdate.route + "/{cardId}/{chequeId}", arguments = listOf(
            navArgument("cardId") { type = NavType.IntType },
            navArgument("chequeId") {type = NavType.IntType}
        )) {
            val parentEntry = remember {
                navController.getBackStackEntry("Parent")
            }
            val cardId = it.arguments!!.getInt("cardId")
            val chequeId = it.arguments!!.getInt("chequeId")
            val transactionsViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            TransactionForm(
                _transactionNames = transactionsViewModel.transactionNames,
                _transactionPrices = transactionsViewModel.transactionPrices,
                _transactionDate = transactionsViewModel.chequeDate.value,
                onGoBack = {
                    navController.navigateUp()
                    transactionsViewModel.clearTransactionsInfo()
                    mainScreenViewModel.currentDestination.value = NavigationRoutes.MainScreen
                },
                onSave = { names, date, prices ->
                    transactionsViewModel.createOrUpdateCheque(names, date, prices, cardId, chequeId)
                }
            )
        }
    }

}



