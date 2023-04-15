package com.example.myapplication.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.GraphFilterOptions
import com.example.myapplication.data.models.UIState
import com.example.myapplication.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class GraphsViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
): ViewModel() {
    var initialData = MutableStateFlow<List<Cheque>>(emptyList())
    var uiState = MutableStateFlow<UIState>(UIState.Loading)
    val data = mutableStateMapOf<Int, Double>()
    val overallSum = MutableStateFlow<Int>(0)
    val filter = MutableStateFlow(FILTER_OPTION[0])



    suspend fun retrieveAllEntries(cardId: Int){
        repositoryImpl.getAllCheques(cardId).collectLatest {
            initialData.emit(it)
            uiState.emit(UIState.Success)
        }
    }

    fun filterDataByMonth(){
        viewModelScope.launch {
            data.clear()
            overallSum.value = 0
            uiState.emit(UIState.Loading)
            for(i in 1..12){
                val filteredByMonth = initialData.value.filter {cheque ->
                    cheque.date.monthValue == i
                }
                var totalSum = 0.0
                filteredByMonth.forEach {cheque ->
                    totalSum += cheque.total
                }
                if(totalSum > 0){
                    data[i] = totalSum
                }
                overallSum.emit(overallSum.value + totalSum.roundToInt())
            }
            uiState.emit(UIState.Success)
        }

    }

    fun filterDataByWeek(){
        viewModelScope.launch {
            data.clear()
            overallSum.value = 0
            uiState.emit(UIState.Loading)
            val now = LocalDate.now()
            var mondayDate = if(now.dayOfWeek != DayOfWeek.MONDAY){
                now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
            }else{
                now
            }
            for(i in 1..7) {
                val filteredByDay = initialData.value.filter { cheque ->
                    cheque.date.dayOfYear == mondayDate.dayOfYear
                }
                var totalSum = 0.0
                filteredByDay.forEach { cheque ->
                    totalSum += cheque.total
                }
                if (totalSum > 0) {
                    data[i] = totalSum
                }
                overallSum.emit(overallSum.value + totalSum.roundToInt())
                mondayDate = mondayDate.plusDays(1)

            }
            uiState.emit(UIState.Success)
        }
    }


    companion object{
        val FILTER_OPTION = listOf(GraphFilterOptions.Month(), GraphFilterOptions.Week())
    }

}