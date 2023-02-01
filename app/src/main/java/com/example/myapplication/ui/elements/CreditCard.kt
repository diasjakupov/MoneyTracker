package com.example.myapplication.ui.elements

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.myapplication.R
import com.example.myapplication.data.models.CreditCardTypes
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CreditCardView(
    cardName: String = "Askar Chort",
    cardType: CreditCardTypes = CreditCardTypes.Visa,
    cardNumber: String = "1234123412341234",
    cardColor: Color = Color.Blue,
    cardId: Int,
    onDotsClick: (
        cardName: String, cardType: CreditCardTypes, cardNumber: String, cardColor: Color, cardId:Int
    ) -> Unit
) {
    val cardOptions = remember {
        mutableStateOf(false)
    }

    //TODO create a popup menu with edit and delete
    if(cardOptions.value){
        Popup() {
            Column() {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {

                }
            }
        }
    }

    Surface(
        color = cardColor,
        shape = RoundedCornerShape(size = 12.dp),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
                .fillMaxWidth()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(cardName, style = typography.h5)
                IconButton(onClick = {

                    onDotsClick(cardName, cardType, cardNumber, cardColor, cardId)
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "three dots", modifier = Modifier.size(34.dp)
                    )
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

