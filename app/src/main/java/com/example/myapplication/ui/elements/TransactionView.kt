package com.example.myapplication.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.TransactionModel
import java.time.LocalDate

@Composable
fun TransactionView(
    transactions: List<TransactionModel>,
    date: LocalDate,
    total: Double,
    onTransactionEdit: () -> Unit,
    onDeleteById: () -> Unit
) {
    val transactionOption = remember {
        mutableStateOf(false)
    }
    val sizeOfView = remember {
        mutableStateOf(IntSize.Zero)
    }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp))
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 12.dp)
            .onSizeChanged {
                sizeOfView.value = it
            }
    ) {
        Column() {

            Box() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cheque:", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    IconButton(onClick = {
                        transactionOption.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Transaction Settings",
                            modifier = Modifier
                                .height(30.dp)
                        )
                    }

                }

                DropdownMenu(
                    offset = DpOffset(
                        x = (sizeOfView.value.width.div(density.density) - 100).dp,
                        y = (-50).dp
                    ),
                    expanded = transactionOption.value,
                    onDismissRequest = { transactionOption.value = false }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                onTransactionEdit()
                                transactionOption.value = false
                            }
                    ) {
                        Text("Edit")
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                onDeleteById()
                                transactionOption.value = false
                            }
                    ) {
                        Text("Delete")
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }




            transactions.forEach { tr ->
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = tr.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500,
                            maxLines = 1,
                            modifier = Modifier.widthIn(max = 250.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${date.dayOfMonth}/${date.monthValue}/${date.year}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(text = "$ ${tr.price}", fontWeight = FontWeight.W400)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                }
            }




            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "$total")
            }
        }


    }
}







