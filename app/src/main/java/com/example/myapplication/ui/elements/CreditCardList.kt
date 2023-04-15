package com.example.myapplication.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.CreditCardTypes
import com.example.myapplication.data.models.UIState
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreditCardList(
    creditCards: List<CreditCardModel>,
    UIState: UIState,
    pagerState: PagerState,
    onCardDelete: (id: Int) -> Unit,
    onCardEdit: (cardName: String, cardType: CreditCardTypes, cardNumber: String, cardColor: Color, cardId: Int) -> Unit
) {
    when (UIState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()

            }
        }
        is UIState.Success -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (creditCards.isNotEmpty()) {
                    HorizontalPager(
                        count = creditCards.size,
                        itemSpacing = 16.dp,
                        state = pagerState
                    ) { idx ->
                        CreditCardView(
                            cardName = creditCards[idx].name,
                            cardNumber = creditCards[idx].cardNumber,
                            cardType = CreditCardTypes.getClass(creditCards[idx].type),
                            cardColor = Color(creditCards[idx].color),
                            cardId = creditCards[idx].id,
                            onCardEdit = { cardName, cardType, cardNumber, cardColor, cardId ->
                                onCardEdit(cardName, cardType, cardNumber, cardColor, cardId)
                            },
                            onDeleteById = { id ->
                                onCardDelete(id)
                            }
                        )
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(110.dp), contentAlignment = Alignment.Center
                    ) {
                        Text("Add your first card!")
                    }
                }


                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        is UIState.Error -> {

        }
    }

}