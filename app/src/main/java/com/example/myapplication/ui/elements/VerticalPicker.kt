package com.example.myapplication.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.myapplication.data.models.CreditCardTypes
import com.example.myapplication.data.models.Type
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun VerticalPicker(
    textValue: String,
    listOfValues: List<Type>,
    modifier: Modifier,
    onValueChange: (text: Type) -> Unit
) {
    val isDropDownShown: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    Box() {
        if (isDropDownShown.value) {
            Popup(
                onDismissRequest = { isDropDownShown.value = false },
                alignment = Alignment.CenterStart,
                offset = IntOffset(y = 40, x = 0),
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
            ) {
                SelectionList(textValue = textValue, listOfValues = listOfValues, modifier = modifier, onValueChange = {
                    onValueChange(it)
                    isDropDownShown.value = false
                })
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(textValue)
        IconButton(
            onClick = { isDropDownShown.value = true },
            enabled = !isDropDownShown.value
        ) {
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = "Arrow icon"
            )
        }
    }
}


@Composable
@Preview
fun VerticalPicker_Preview() {
    MyApplicationTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            VerticalPicker(
                CreditCardTypes.MasterCard.name,
                listOfValues = listOf<Type>(CreditCardTypes.MasterCard, CreditCardTypes.Visa),
                Modifier
            ) {}
        }
    }
}