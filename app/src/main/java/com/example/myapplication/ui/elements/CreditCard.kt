package com.example.myapplication.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.data.models.CreditCardTypes

@Composable
fun CreditCardView(
    cardName: String = "Askar Chort",
    cardType: CreditCardTypes = CreditCardTypes.Visa,
    cardNumber: String = "1234123412341234",
    cardColor: Color = Color.Blue,
    cardId: Int,
    onCardEdit: (
        cardName: String, cardType: CreditCardTypes, cardNumber: String, cardColor: Color, cardId:Int
    ) -> Unit,
    onDeleteById: (id: Int)->Unit
) {
    val cardOptions = remember {
        mutableStateOf(false)
    }
    val sizeOfView = remember {
        mutableStateOf(IntSize.Zero)
    }
    val density = LocalDensity.current



    Surface(
        color = cardColor,
        shape = RoundedCornerShape(size = 12.dp),
        contentColor = Color.White
    ) {


            Column(
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
                    .fillMaxWidth()
                    .onSizeChanged {
                        sizeOfView.value = it
                    }
            ) {

                Box(){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(cardName, style = typography.h5)
                        IconButton(onClick = {
                            cardOptions.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "three dots", modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        offset = DpOffset(
                            x = (sizeOfView.value.width.div(density.density) - 100).dp,
                            y = (-50).dp
                        ),
                        expanded = cardOptions.value,
                        onDismissRequest = { cardOptions.value = false }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .width(100.dp)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable {
                                    onCardEdit(cardName, cardType, cardNumber, cardColor, cardId)
                                    cardOptions.value = false
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
                                    onDeleteById(cardId)
                                    cardOptions.value = false
                                }
                        ) {
                            Text("Delete")
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = when (cardType) {
                            is CreditCardTypes.MasterCard -> {
                                painterResource(id = R.drawable.mastercard)
                            }
                            is CreditCardTypes.Visa -> {
                                painterResource(id = R.drawable.visa)
                            }

                        },
                        contentDescription = "card type image", contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 100.dp, height = 50.dp)
                    )
                    Text(text = "Balance: $5,000", style = typography.h6)
                }

                Text(
                    cardNumber,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text("Credit limit: $500")
                    Text("Valid till: 12/09/20")
                }
            }

        }

    }


