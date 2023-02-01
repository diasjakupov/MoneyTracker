package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.models.CreditCardTypes
import com.example.myapplication.ui.elements.BottomSheetColorPicker
import com.example.myapplication.ui.elements.VerticalPicker
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreditCardForm(
    _cardName: String = "",
    _cardType: CreditCardTypes = CreditCardTypes.MasterCard,
    _cardNumber: String = "",
    _cardColor: Color = Color.Red,
    onGoBack: () -> Unit,
    onSave: (cardName: String,
             type: String,
             cardNumber: String,
             color: ULong)->Unit
) {
    val cardName: MutableState<String> = remember {
        mutableStateOf(_cardName)
    }
    val cardType: MutableState<CreditCardTypes> = remember {
        mutableStateOf(_cardType)
    }
    val cardNumber: MutableState<String> = remember {
        mutableStateOf(_cardNumber)
    }

    val creditTypes = remember {
        listOf<CreditCardTypes>(CreditCardTypes.MasterCard, CreditCardTypes.Visa)
    }

    val pickedColor = remember {
        mutableStateOf(_cardColor)
    }

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()


    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetColorPicker(pickedColor.value) {
                pickedColor.value = it
            }
        },
        sheetState = bottomSheetScaffoldState,
        sheetElevation = 16.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {

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
                            modifier = Modifier.size(40.dp), tint = MaterialTheme.colors.Link
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
                        "Card Information: ".uppercase(),
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(start = 12.dp)
                    ) {
                        TextField(
                            value = cardName.value,
                            onValueChange = { cardName.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = {
                                Text("Card Name")
                            },
                            label = {
                                Text("Enter card name:")
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = Color.LightGray
                            )
                        )

                        TextField(
                            value = cardNumber.value,
                            onValueChange = {
                                if (it.length <= 16) {
                                    cardNumber.value = it
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("1234-1234-1234-1234")
                            },
                            label = {
                                Text("Enter card number:")
                            },
                            singleLine = true,
                            visualTransformation = VisualTransformation {
                                val trimmed =
                                    if (it.text.length >= 16) it.text.substring(0..15) else it.text
                                var out = ""
                                for (i in trimmed.indices) {
                                    out += trimmed[i]
                                    if (i % 4 == 3 && i != 15) out += "-"
                                }

                                val creditCardOffsetTranslator = object : OffsetMapping {
                                    override fun originalToTransformed(offset: Int): Int {
                                        if (offset <= 3) return offset
                                        if (offset <= 7) return offset + 1
                                        if (offset <= 11) return offset + 2
                                        if (offset <= 16) return offset + 3
                                        return 19
                                    }

                                    override fun transformedToOriginal(offset: Int): Int {
                                        if (offset <= 4) return offset
                                        if (offset <= 9) return offset - 1
                                        if (offset <= 14) return offset - 2
                                        if (offset <= 19) return offset - 3
                                        return 16
                                    }
                                }

                                return@VisualTransformation TransformedText(
                                    AnnotatedString(out), creditCardOffsetTranslator
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = Color.LightGray
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = "",
                                label = {
                                    Text("Type:")
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
                            VerticalPicker(
                                textValue = cardType.value.name,
                                listOfValues = creditTypes,
                                modifier = Modifier.width(150.dp)
                            ) {
                                cardType.value = it as CreditCardTypes
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

                Column() {
                    Text(
                        "Card Visuals:  ".uppercase(),
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = "",
                            label = {
                                Text("Color:")
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
                        Box(modifier = Modifier
                            .border(2.dp, Color.Black, CircleShape)
                            .clip(
                                CircleShape
                            )
                            .background(pickedColor.value)

                            .width(25.dp)
                            .height(25.dp)
                            .clickable {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.show()
                                }
                            }

                        )
                    }
                }


                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight())
                    Button(
                        onClick = {
                            onSave(cardName.value,
                                cardType.value.name,
                                cardNumber.value,
                                pickedColor.value.value)
                            onGoBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.ApplyColor,
                            contentColor = MaterialTheme.colors.ApplyColorText
                        ), elevation = ButtonDefaults.elevation(defaultElevation = 12.dp)
                    ) {
                        Text("Save".uppercase(), fontSize = 20.sp)
                    }
                }

            }

        }


    }


}
