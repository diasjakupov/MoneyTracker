package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.elements.*
import com.example.myapplication.ui.navigation.NavigationRoutes
import com.example.myapplication.ui.theme.FormBackgroundColor
import com.example.myapplication.ui.viewmodels.CreditCardViewModel
import com.example.myapplication.ui.viewmodels.MainScreenViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    transactionViewModel: TransactionsViewModel,
    creditCardViewModel: CreditCardViewModel,
    mainScreenViewModel: MainScreenViewModel
) {
    val creditCards =
        creditCardViewModel.creditCards.collectAsState(initial = arrayListOf())
    val chequeList = transactionViewModel.chequeList.collectAsState(initial = listOf())

    val pagerState = rememberPagerState()
    val cardsState = creditCardViewModel.uiState.collectAsState()

    //TODO Loading bar on cheque list

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        transactionViewModel.getAllChequeByCard(pagerState.currentPage)
        creditCardViewModel.getAllCreditCards()
    })

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.FormBackgroundColor)
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 60.dp)
    ) {
        //ToolBar
        Header(onClick = {
            mainScreenViewModel.currentDestination.value = NavigationRoutes.CreditCardForm
        })

        //body
        CreditCardList(
            creditCards = creditCards.value,
            pagerState = pagerState, UIState = cardsState.value
        ) { cardName, cardType, cardNumber, cardColor, cardId ->
            creditCardViewModel.setDefaultValuesForCreditCard(
                cardName,
                cardType,
                cardNumber,
                cardColor,
                cardId
            )
            mainScreenViewModel.currentDestination.value = NavigationRoutes.CreditCardFormUpdate
        }

        TransactionList(
            cheques = chequeList.value.reversed(),
            isCardListEmpty = creditCards.value.isEmpty(),
            onAddTransaction = {
                mainScreenViewModel.currentDestination.value = NavigationRoutes.TransactionForm
            },
            onFilter = { filterOptions ->
                transactionViewModel.filterChequeList(filterOptions)
            },
            onTransactionEdit = { transactions, date, idx ->
                transactionViewModel.setDefaultChequeInfo(transactions, date, idx)
                mainScreenViewModel.currentDestination.value =
                    NavigationRoutes.TransactionFormUpdate
            }, onDelete = { idx ->
                transactionViewModel.deleteById(idx)
            })

        LaunchedEffect(key1 = mainScreenViewModel.currentDestination.value, block = {
            when (mainScreenViewModel.currentDestination.value) {
                is NavigationRoutes.CreditCardForm -> {
                    navController.navigate(NavigationRoutes.CreditCardForm.route)
                }
                is NavigationRoutes.TransactionForm -> {
                    navController.navigate(NavigationRoutes.TransactionForm.route + "/${pagerState.currentPage}")
                }
                is NavigationRoutes.TransactionFormUpdate -> {
                    navController.navigate(NavigationRoutes.TransactionFormUpdate.route + "/${pagerState.currentPage}/${transactionViewModel.chequeID.value}")
                }
                is NavigationRoutes.CreditCardFormUpdate -> {
                    navController.navigate(
                        NavigationRoutes.CreditCardFormUpdate.route + "/${pagerState.currentPage}"
                    )
                }
                is NavigationRoutes.MainScreen -> {
                    creditCardViewModel.creditCardClear()
                }
                else -> {}
            }
        })
    }
}


