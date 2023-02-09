package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.UIState
import com.example.myapplication.ui.elements.*
import com.example.myapplication.ui.navigation.NavigationRoutes
import com.example.myapplication.ui.theme.FormBackgroundColor
import com.example.myapplication.ui.viewmodels.CreditCardViewModel
import com.example.myapplication.ui.viewmodels.MainScreenViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    transactionViewModel: TransactionsViewModel,
    creditCardViewModel: CreditCardViewModel,
    mainScreenViewModel: MainScreenViewModel
) {
    val pagerState = rememberPagerState()
    val cardsState = creditCardViewModel.uiState.collectAsState()
    val transactionState = transactionViewModel.uiState.collectAsState()

    val creditCards =
        produceState(initialValue = listOf<CreditCardModel>(), producer = {
            creditCardViewModel.creditCards.collectLatest {
                creditCardViewModel.uiState.value = UIState.Success
                value = it
            }
        })
    val currentCard =
        produceState(initialValue = -1, pagerState.currentPage, creditCards.value) {
            if (creditCards.value.isNotEmpty()) {
                value = creditCards.value[pagerState.currentPage].id
            }
        }
    val chequeList = produceState<List<Cheque>>(
        initialValue = arrayListOf<Cheque>(),
        currentCard.value,
        producer = {
            if (value.isEmpty() || currentCard.value != -1) {
                transactionViewModel.getAllChequeByCard(currentCard.value)
                    .collectLatest {
                        value = it
                    }
            }
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
            pagerState = pagerState, UIState = cardsState.value,
            onCardEdit = { cardName, cardType, cardNumber, cardColor, cardId ->
                creditCardViewModel.setDefaultValuesForCreditCard(
                    cardName,
                    cardType,
                    cardNumber,
                    cardColor,
                    cardId
                )
                mainScreenViewModel.currentDestination.value = NavigationRoutes.CreditCardFormUpdate
            },
            onCardDelete = { id ->
                creditCardViewModel.deleteById(id)
                transactionViewModel.uiState.value = UIState.Loading
            }
        )

        TransactionList(
            cheques = chequeList.value.reversed(),
            isCardListEmpty = creditCards.value.isEmpty(),
            uiState = transactionState.value,
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
                    navController.navigate(NavigationRoutes.TransactionForm.route + "/${currentCard.value}")
                }
                is NavigationRoutes.TransactionFormUpdate -> {
                    navController.navigate(NavigationRoutes.TransactionFormUpdate.route + "/${currentCard.value}/${transactionViewModel.chequeID.value}")
                }
                is NavigationRoutes.CreditCardFormUpdate -> {
                    navController.navigate(
                        NavigationRoutes.CreditCardFormUpdate.route + "/${currentCard.value}"
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


