package com.example.myapplication.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.models.GraphFilterOptions
import com.example.myapplication.data.models.UIState
import com.example.myapplication.ui.elements.SelectionList
import com.example.myapplication.ui.elements.VerticalPicker
import com.example.myapplication.ui.theme.FormBackgroundColor
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.GraphsViewModel
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.Month
import kotlin.math.roundToInt

fun Int.toShortsString(): String {
    var simplify = ""
    var am: Float = 0.0f
    val df = DecimalFormat("##.##")
    if (kotlin.math.abs(this / 1000000000) >= 1) {
        am = this.toFloat() / 1000000000
        simplify = df.format(am) + "B"
    } else if (kotlin.math.abs(this / 1000000) >= 1) {
        am = this.toFloat() / 1000000
        simplify = df.format(am) + "M"
    } else if (kotlin.math.abs(this / 1000) >= 1) {
        am = this.toFloat() / 1000
        simplify = df.format(am) + "K"
    } else {
        simplify = this.toString()
    }
    return simplify

}

@Composable
fun DataGraphScreen(viewModel: GraphsViewModel) {
    val initialData = viewModel.initialData.collectAsState()
    val overallSum = viewModel.overallSum.collectAsState()
    val filter = viewModel.filter.collectAsState()

    val data = viewModel.data
    val uiState = viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {
        viewModel.retrieveAllEntries(1) // TODO remove hardcore cardID
    })

    LaunchedEffect(key1 = initialData.value, key2 = filter.value, block = {
        if (initialData.value.isNotEmpty()) {
            when(filter.value){
                is GraphFilterOptions.Month->{
                    viewModel.filterDataByMonth()
                }
                is GraphFilterOptions.Week->{
                    viewModel.filterDataByWeek()
                }
            }
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.FormBackgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Graph(
                modifier = Modifier
                    .fillMaxWidth(),
                data = data,
                label = filter.value
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Total: ${overallSum.value}",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
            )
            VerticalPicker(textValue = filter.value.name, listOfValues = GraphsViewModel.FILTER_OPTION, modifier = Modifier.width(150.dp), onValueChange = {
                coroutineScope.launch {
                    viewModel.filter.emit(it as GraphFilterOptions)
                }
            })
        }

    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun Graph(
    modifier: Modifier,
    data: SnapshotStateMap<Int, Double>,
    label: GraphFilterOptions
) {
    val textMeasurer = rememberTextMeasurer()
    val sortedYValues = data.values.sorted().toSet()
    val sortedXValues = data.keys.sorted().toSet()


    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (sortedXValues.isNotEmpty()) {
            Canvas(
                modifier = Modifier
                    .height(((sortedXValues.size + 1) * (170f.div(LocalDensity.current.density))).dp)
                    .fillMaxWidth()
            ) {
                val numberOfEl = sortedXValues.size.let {
                    if (it > 3) {
                        it
                    } else {
                        it + 1
                    }
                }
                val xAxisSpace = (size.width - 100f) / (numberOfEl)
                val yAxisSpace = minOf( 170f)
                //x-axis
                drawLine(
                    Color.Black,
                    start = Offset(x = 120f, y = size.height - 70f),
                    end = Offset(x = size.width - 30f, y = size.height - 70f),
                    strokeWidth = 5f
                )

                //y-axis
                drawLine(
                    Color.Black,
                    start = Offset(x = 120f, y = size.height - 70f),
                    end = Offset(x = 120f, y = size.height - 70f - yAxisSpace * sortedYValues.size),
                    strokeWidth = 5f
                )

                //x-axis labels
                sortedXValues.forEachIndexed { idx, it ->
                    val text = when (label) {
                        is GraphFilterOptions.Month -> {
                            Month.of(it).name.slice(0 until (if (sortedXValues.size > 7) 2 else 3))
                        }
                        is GraphFilterOptions.Week -> {
                            DayOfWeek.of(it).name.slice(0 until (if (sortedXValues.size > 7) 2 else 3))
                        }
                        else -> {
                            ""
                        }
                    }
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        topLeft = Offset(x = xAxisSpace * (idx + 1) , y = size.height - 50f),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    )
                }
                //y-axis labels
                sortedYValues.forEachIndexed { idx, text ->
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text.roundToInt().toShortsString(),
                        topLeft = Offset(x = 0f, y = size.height - 70f - yAxisSpace * (idx + 1)),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    )
                    drawLine(
                        Color.LightGray,
                        start = Offset(
                            x = 100f,
                            y = size.height - (70f - 12.sp.toPx()) - yAxisSpace * (idx + 1)
                        ),
                        end = Offset(
                            x = size.width - 50f,
                            y = size.height - (70f - 12.sp.toPx()) - yAxisSpace * (idx + 1)
                        ),
                        strokeWidth = 1f
                    )
                }

                //graph
                sortedXValues.forEachIndexed { xIdx, key ->
                    val yIdx = sortedYValues.indexOf(data[key])

                    drawCircle(
                        color = Color.Black, radius = 10f, center = Offset(
                            x = xAxisSpace * (xIdx + 1) + 12.sp.toPx(),
                            y = size.height - 40f - yAxisSpace * (yIdx + 1)
                        )
                    )
                    if (xIdx < (sortedXValues.size - 1)) {
                        val nextYIdx = sortedYValues.indexOf(
                            data[sortedXValues.elementAt(
                                xIdx + 1
                            )]
                        )
                        drawLine(
                            color = Color.Black,
                            start = Offset(
                                x = xAxisSpace * (xIdx + 1) + 12.sp.toPx(),
                                y = size.height - 40f - yAxisSpace * (yIdx + 1)
                            ),
                            end = Offset(
                                x = xAxisSpace * (xIdx + 2) + 12.sp.toPx(),
                                y = size.height - 40f - yAxisSpace * (nextYIdx + 1)
                            ), strokeWidth = 3f

                        )
                    }
                }

            }
        } else {
            Text(text = "No data on this", fontSize = 18.sp, color = Color.LightGray)
        }

    }


}


@Composable
@Preview
fun DataGraphScreen_Preview() {
    MyApplicationTheme {
        val viewModel = hiltViewModel<GraphsViewModel>()
        DataGraphScreen(viewModel)
    }
}