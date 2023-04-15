package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.TransactionsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TransactionForm(
    viewModel: TransactionsViewModel,
    cardId: Int,
    chequeId: Int,
    onGoBack: () -> Unit,
    onQRScan: ()->Unit
) {
    val transactionName = remember {
        viewModel.transactionNames
    }
    val transactionPrice = remember {
        viewModel.transactionPrices
    }
    val transactionDate = remember {
        mutableStateOf(viewModel.chequeDate)
    }

    //Date Picker Dialog
    val dialogState = rememberMaterialDialogState()

//    DisposableEffect(key1 = true, effect = {
//        onDispose {
//            onGoBack()
//        }
//    })

    LaunchedEffect(key1 = true, block = {
        viewModel.parseDataToTransaction()
    })

    MaterialDialog(dialogState = dialogState, buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }, autoDismiss = true) {
        this.datepicker() {
            transactionDate.value = it
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.FormBackgroundColor)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { onGoBack() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowLeft,
                        contentDescription = "Arrow Left",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colors.Link
                    )
                    Text("Go Back", color = MaterialTheme.colors.Link)
                }


            }
            Spacer(modifier = Modifier.fillMaxWidth())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column() {
                Text(
                    "Transaction Information: ".uppercase(),
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(transactionName.keys.toList(), key = { it }) { idx ->

                        val swipeState = rememberDismissState(confirmStateChange = {
                            when (it) {
                                DismissValue.DismissedToStart -> {
                                    transactionName.remove(idx)
                                    transactionPrice.remove(idx)
                                    true
                                }
                                else -> false
                            }
                        })

                        SwipeToDismiss(
                            state = swipeState,
                            background = {
                                SwipeBackground(dismissState = swipeState)
                            },
                            dismissThresholds = { direction ->
                                FractionalThreshold(
                                    if (direction == DismissDirection.EndToStart) 0.4f else 0.8f
                                )
                            },
                            modifier = Modifier.animateItemPlacement(),
                            directions = setOf(DismissDirection.EndToStart)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(start = 12.dp)

                            ) {
                                TextField(
                                    value = transactionName[idx].let { name ->
                                        if (name.isNullOrBlank()) {
                                            ""
                                        } else {
                                            name
                                        }
                                    },
                                    onValueChange = {
                                        transactionName[idx] = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    placeholder = {
                                        Text("Product Name")
                                    },
                                    label = {
                                        Text("Enter product name:")
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.LightGray,
                                        focusedIndicatorColor = Color.LightGray
                                    )
                                )

                                TextField(
                                    value = (transactionPrice[idx].let { price ->
                                        if (price == "0.0" || price.isNullOrBlank()) {
                                            ""
                                        } else {
                                            price.toString()
                                        }
                                    }),
                                    onValueChange = {
                                        transactionPrice[idx] = it
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text("999.99")
                                    },
                                    label = {
                                        Text("Enter price:")
                                    },
                                    singleLine = true,
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    )
                                )

                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.fillMaxHeight())
                            Button(
                                onClick = {
                                    Log.e("TAG SIZE", "${transactionName.size}")
                                    transactionName[transactionName.size + 1] = ""
                                    transactionPrice[transactionName.size + 1] = "0.0"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.ApplyColor,
                                    contentColor = MaterialTheme.colors.ApplyColorText
                                ),
                                elevation = ButtonDefaults.elevation(defaultElevation = 12.dp)
                            ) {
                                Text("+ One".uppercase(), fontSize = 20.sp)
                            }
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = "",
                    label = {
                        Text("Date:")
                    },
                    modifier = Modifier.width(100.dp),
                    readOnly = true,
                    enabled = false,
                    onValueChange = {},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledLabelColor = Color.Gray
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${transactionDate.value.dayOfMonth}/${transactionDate.value.monthValue}/${transactionDate.value.year}")
                    IconButton(onClick = { dialogState.show() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = "Arrow icon"
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ){
                IconButton(onClick = {
                    println("PRINT")
                    onQRScan()
                },
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        ) {
                    Icon(imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "qr code scanner",
                        modifier = Modifier.size(40.dp))
                }
            }


            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.fillMaxHeight())
                Button(
                    onClick = {
                        viewModel.createOrUpdateCheque(
                            names = transactionName.filter {
                                it.value != ""
                            },
                            date = transactionDate.value,
                            price = transactionPrice,
                            cardId = cardId,
                            chequeId = chequeId
                        )

                        onGoBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.ApplyColor,
                        contentColor = MaterialTheme.colors.ApplyColorText
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 12.dp)
                ) {
                    Text("Save".uppercase(), fontSize = 20.sp)
                }
            }



        }
    }
}


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeBackground(dismissState: DismissState) {
    val icon = when (dismissState.dismissDirection) {
        DismissDirection.EndToStart -> Icons.Default.Delete
        else -> {
            Icons.Default.Transgender
        }
    }
    val background by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.DismissedToStart -> {
                Color.Red
            }
            else -> {
                Color.Red
            }
        }
    )


    Box(
        Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .background(background, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.padding(40.dp)
        )
    }
}