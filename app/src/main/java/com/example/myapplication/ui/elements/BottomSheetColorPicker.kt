package com.example.myapplication.ui.elements

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun BottomSheetColorPicker(pickedColor: Color, onColorPicked: (Color) -> Unit) {
    val colors: List<Color> = remember {
        val items = mutableListOf<Color>()

        for (i in 0..360) {
            items.add(Color.hsv(i.toFloat(), 1f, 1f))
        }

        items
    }
    val textRepresentedColor = remember {
        mutableStateOf("")
    }


    val isErrorOccurred = remember {
        mutableStateOf(false)
    }

    val offsetX = remember {
        mutableStateOf(0f)
    }
    val offsetY = remember {
        mutableStateOf(0f)
    }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = textRepresentedColor.value,
                onValueChange = {
                    if (it.length <= 6) {
                        textRepresentedColor.value = it.uppercase()
                    }

                    if (it == "FFFFFF") {
                        isErrorOccurred.value = true
                    }
                },
                modifier = Modifier
                    .width(125.dp),
                placeholder = {
                    Text("Hex: FFFFFF")
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textRepresentedColor.value.length == 6) {
                            try {
                                onColorPicked(
                                    Color(
                                        android.graphics.Color.parseColor(
                                            "#" + textRepresentedColor.value
                                        )
                                    )
                                )
                                focusManager.clearFocus()
                                isErrorOccurred.value = false
                            } catch (e: java.lang.NumberFormatException) {
                                isErrorOccurred.value = true
                            }

                        }
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black
                ), textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp)
            )

            if (isErrorOccurred.value) {
                Text(
                    "Unknown color",
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(25.dp))


        Box(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .background(brush = Brush.horizontalGradient(colors))
                .clipToBounds()
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX.value =
                                    (offsetX.value + dragAmount.x).coerceIn(
                                        (-10f) * this.density,
                                        220f * this.density
                                    )
                                offsetY.value =
                                    (offsetY.value + dragAmount.y).coerceIn(0f, 220f * this.density)
                            },
                            onDragEnd = {
                                onColorPicked(
                                    colors[((360.0 / (250.0 * this.density)) * (offsetX.value + 15 * this.density)).toInt()]
                                )
                                textRepresentedColor.value = ""
                            })

                    }
                    .width(30.dp)
                    .height(30.dp)

                    .background(Color.White), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                        .background(pickedColor)
                )
            }
        }




        Spacer(modifier = Modifier.height(50.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.6f)
//                .height(80.dp)
//                .background(pickedColor)
//        )
    }


}


@Composable
@Preview
fun BottomSheetColorPicker_Preview() {
    MyApplicationTheme {
        BottomSheetColorPicker(Color.Black) {}
    }
}