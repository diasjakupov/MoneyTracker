package com.example.myapplication.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.FilterOptions
import com.example.myapplication.data.models.TransactionModel
import com.example.myapplication.ui.navigation.NavigationRoutes
import com.example.myapplication.ui.theme.FormBackgroundColor
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.time.LocalDate

@Composable
fun TransactionList(
    cheques: List<Cheque>,
    isCardListEmpty: Boolean,
    onAddTransaction: () -> Unit,
    onFilter: (filterOptions: FilterOptions) -> Unit,
    onTransactionEdit: (transactions: List<TransactionModel>, date: LocalDate, id:Int)->Unit,
    onDelete: (id:Int)->Unit
) {
    val isFilterShown = remember() {
        mutableStateOf(false)
    }
    val filterOptions = remember {
        listOf(FilterOptions.SORT_ASC(), FilterOptions.SORT_DESC())
    }

    if (isFilterShown.value) {
        Dialog(onDismissRequest = { isFilterShown.value = false }) {
            SelectionList(
                textValue = "",
                listOfValues = filterOptions,
                modifier = Modifier,
                onValueChange = {
                    onFilter(it as FilterOptions)
                    isFilterShown.value = false
                })
        }
    }


    Column() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ButtonI(
                icon = Icons.Default.Add,
                contentDescription = "Add Icon",
                text = "Transaction",
                enabled = !isCardListEmpty
            ) {
                onAddTransaction()
            }
            Spacer(modifier = Modifier.width(12.dp))
            ButtonI(
                icon = Icons.Default.FilterList,
                contentDescription = "Filter Icon",
                text = "Filter",
                enabled = !isCardListEmpty
            ) {
                isFilterShown.value = true
            }

        }
        if (cheques.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cheques.size) { idx ->
                    TransactionView(
                        transactions = cheques[idx].transactions, date = cheques[idx].date, total = cheques[idx].total,
                        onTransactionEdit = {
                            onTransactionEdit(cheques[idx].transactions, cheques[idx].date, cheques[idx].id)
                        }, onDeleteById = {
                            onDelete(cheques[idx].id)
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Add your first transaction")
            }

        }
    }

}

