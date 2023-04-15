package com.example.myapplication.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.*
import com.example.myapplication.data.network.NetResponse
import com.example.myapplication.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch
import java.time.LocalDate

import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {
    var chequeList = MutableStateFlow<List<Cheque>>(listOf())
    val transactionNames = mutableStateMapOf<Int, String>()
    val transactionPrices = mutableStateMapOf<Int, String>()
    var chequeDate: LocalDate = LocalDate.now()
    val chequeID = mutableStateOf(-1)
    val uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val isFiltered = mutableStateOf<FilterOptions>(FilterOptions.SORT_ASC())
    val fetchedTransactions =  MutableStateFlow<NetResponse<RemoteCheque?>?>(null)


    private fun filterChequeList(filterOptions: FilterOptions,
                                 data: List<Cheque>) {
            when (filterOptions) {
                is FilterOptions.SORT_DESC -> {
                    chequeList.value = data.sortedByDescending { it.total }
                }
                is FilterOptions.SORT_ASC -> {
                    chequeList.value = data.sortedBy { it.total }
                }
            }
            isFiltered.value = filterOptions

    }

    suspend fun getAllChequeByCard(
        id: Int,
        onCollect: (List<Cheque>)->Unit
    ) {
        uiState.value = UIState.Success
        repositoryImpl.getAllCheques(id).collectLatest {
            chequeList.emit(it)
            filterChequeList(isFiltered.value, it)
            onCollect(chequeList.value)
        }
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
            if(transaction.isNotEmpty()){
                if (chequeId == -1) {
                    repositoryImpl.createCheque(
                        cardId, transaction, date
                    )
                } else {
                    repositoryImpl.updateCheque(
                        cardId, transaction, date, chequeId
                    )
                }
            }else{
                deleteById(chequeId)
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
        chequeDate = date
        chequeID.value = id
    }

    fun clearTransactionsInfo() {
        Log.e("TAG", "Clear tr")
        transactionNames.clear()
        transactionPrices.clear()
        chequeID.value = -1
        fetchedTransactions.value = null
        chequeDate = LocalDate.now()
    }

    fun deleteById(idx: Int) {
        viewModelScope.launch {
            repositoryImpl.deleteChequeById(idx)
        }
    }


    suspend fun parseDataToTransaction(){
        //TODO collecting two or more times in a row
        fetchedTransactions.collectLatest {
            Log.e("TAG", "COLLECTING $it")
            if(it != null){
                when(it){
                    is NetResponse.Successful<RemoteCheque?>->{
                        val data = it.data
                        if(data != null){
                            chequeDate = LocalDate.parse(data.date)
                            var maxOldKey = transactionNames.keys.let {keys->
                                if(keys.isNotEmpty()){
                                    keys.max()+1
                                }else{
                                    1
                                }
                            }
                            data.items.forEach { productItem ->
                                transactionNames[maxOldKey] = productItem.name
                                transactionPrices[maxOldKey] = (productItem.price).toString()
                                maxOldKey++
                            }
                        }
                    }
                    else->{}
                }
            }
        }
    }

    fun getTransactionInfoByQr(url: String){
        viewModelScope.launch(Dispatchers.IO){
            repositoryImpl.getRemoteCheque(url).collectLatest {
                Log.e("TAG", "emitting")
                fetchedTransactions.emit(it)
            }
        }
    }
}