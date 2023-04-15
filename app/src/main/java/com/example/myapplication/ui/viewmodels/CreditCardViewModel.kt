package com.example.myapplication.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.*
import com.example.myapplication.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class CreditCardViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    val creditCardInfo: MutableState<CreditCardModel> = mutableStateOf(
        CreditCardModel(
            "", "", "", 0, Color.Red.toArgb()
        )
    )
    private val _creditCards = repositoryImpl.getAllCreditCards().map {
        it.map { card ->
            val trimmed =
                if (card.cardNumber.length >= 16) card.cardNumber.substring(0..15) else card.cardNumber
            var out = ""
            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 4 == 3 && i != 15) out += " "
            }
            card.cardNumber = out
            card
        }
    }
    val creditCards = _creditCards
    val uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)


    fun creditCardClear() {
        creditCardInfo.value = CreditCardModel("", "", "", 0, Color.Red.toArgb())
    }

    fun setDefaultValuesForCreditCard(
        cardName: String,
        cardType: CreditCardTypes,
        cardNumber: String,
        cardColor: Color,
        cardId: Int
    ) {
        Log.e("HAH", cardColor.toArgb().toString())
        creditCardInfo.value =
            CreditCardModel(
                cardName,
                cardType.name,
                cardNumber.replace(" ", ""),
                0,
                cardColor.toArgb(), cardId
            )
    }


    fun createOrUpdateCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: Int, cardId: Int
    ) {
        Log.e("TAG", color.toString())
        viewModelScope.launch(Dispatchers.IO) {
            if (cardId == -1) {
                repositoryImpl.createCard(cardName, type, cardNumber, color)
            } else {
                repositoryImpl.updateCard(cardName, type, cardNumber, color, cardId)
            }
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch {
            repositoryImpl.deleteCardById(id)
            repositoryImpl.deleteChequeByCardID(id)
        }
    }

}