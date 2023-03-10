package com.example.myapplication.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.FilterOptions
import com.example.myapplication.data.models.TransactionModel
import com.example.myapplication.data.models.UIState
import com.example.myapplication.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import java.time.LocalDate

import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {
    var chequeList = MutableStateFlow<List<Cheque>>(listOf())
    val transactionNames = hashMapOf<Int, String>()
    val transactionPrices = hashMapOf<Int, String>()
    val chequeDate = mutableStateOf(LocalDate.now())
    val chequeID = mutableStateOf(-1)
    val uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)


    fun filterChequeList(filterOptions: FilterOptions) {
        viewModelScope.launch {
            when (filterOptions) {
                is FilterOptions.SORT_DESC -> {
                    chequeList.value = chequeList.value.sortedByDescending { it.total }
                }
                is FilterOptions.SORT_ASC -> {
                    chequeList.value = chequeList.value.sortedBy { it.total }
                }
            }
        }
    }

    fun getAllChequeByCard(
        id: Int
    ): Flow<List<Cheque>> {

        Log.e("TAG", "PAGE: ${id}")
        uiState.value = UIState.Success
        return repositoryImpl.getAllCheques(id)
//            repositoryImpl.getAllCheques(id).collectLatest {
//                uiState.value = UIState.Loading
//                Log.e("TAG", "Start collecting")
//
//                Log.e("TAG", "Transaction list ${it}")
//                delay(2000L)
//                uiState.value = UIState.Success
//                chequeList.value = it
//
//            }

    }

    fun createOrUpdateCheque(
        names: Map<Int, String>,
        date: LocalDate,
        price: SnapshotStateMap<Int, String>,
        cardId: Int, chequeId: Int
    ) {
        viewModelScope.launch {
            val transaction = mutableListOf<TransactionModel>()
            names.forEach { (id, name) ->
                transaction.add(
                    TransactionModel(
                        name, date, price[id].let {
                            if (it.isNullOrBlank()) {
                                0.0
                            } else {
                                it.toDouble()
                            }
                        },
                        cardId = cardId
                    )
                )

            }
            if (chequeId == -1) {
                repositoryImpl.createCheque(
                    cardId, transaction, date
                )
            } else {
                repositoryImpl.updateCheque(
                    cardId, transaction, date, chequeId
                )
            }

        }
    }

    fun setDefaultChequeInfo(
        trans: List<TransactionModel>, date: LocalDate, id: Int
    ) {
        trans.forEachIndexed { idx, model ->
            transactionNames[idx + 1] = model.name
            transactionPrices[idx + 1] = model.price.toString()
        }
        chequeDate.value = date
        chequeID.value = id
    }

    fun clearTransactionsInfo() {
        transactionNames.clear()
        transactionPrices.clear()
        chequeID.value = -1
        chequeDate.value = LocalDate.now()
    }

    fun deleteById(idx: Int) {
        viewModelScope.launch {
            repositoryImpl.deleteChequeById(idx)
        }
    }
}