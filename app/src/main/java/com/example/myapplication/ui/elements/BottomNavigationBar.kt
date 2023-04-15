package com.example.myapplication.ui.elements

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.GRAPH_ROUTE
import com.example.myapplication.ui.navigation.MAIN_SCREEN_ROUTE
import com.example.myapplication.ui.navigation.NavigationRoutes
import com.example.myapplication.ui.theme.ApplyColor
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Teal

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth(0.5f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,  modifier = Modifier.clickable {
                    navController.navigate(MAIN_SCREEN_ROUTE)
                }) {
                    CreditCardIcon(
                        iconColor = if (currentBackStackEntry.value?.destination?.route
                            != NavigationRoutes.MainScreen.route) {
                            MaterialTheme.colors.ApplyColor
                        } else {
                            MaterialTheme.colors.Teal
                        },
                        width = 25.dp,
                        height = 20.dp
                    )
                    Text(text = "Credit Cards", fontSize = 12.sp)
                }

            }
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxWidth()) {

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    navController.navigate(GRAPH_ROUTE)
                }) {
                    HistogramIcon(
                        iconColor = if (currentBackStackEntry.value?.destination?.route
                            != NavigationRoutes.GraphsScreen.route) {
                            MaterialTheme.colors.ApplyColor
                        } else {
                            MaterialTheme.colors.Teal
                        },
                        width = 25.dp,
                        height = 25.dp
                    )
                    Text(text = "Graphs", fontSize = 12.sp)
                }

            }
        }
    }

}


@Composable
fun CreditCardIcon(iconColor: Color, width: Dp, height: Dp) {
    Canvas(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        drawRoundRect(
            color = iconColor,
            topLeft = Offset(0f, this.size.height * 0.1f),
            size = Size(height = this.size.height * 0.20f, width = this.size.width),
            cornerRadius = CornerRadius(0f, 0f)
        )
        drawRoundRect(
            color = iconColor,
            topLeft = Offset(0f, this.size.height * 0.35f),
            size = Size(height = this.size.height * 0.65f, width = this.size.width),
            cornerRadius = CornerRadius(5f, 5f)
        )
        drawCircle(
            color = Color.White,
            center = Offset(5f * this.density, this.size.height * 0.75f),
            radius = 5f
        )
    }
}


@Composable
fun HistogramIcon(iconColor: Color, width: Dp, height: Dp) {
    Canvas(
        modifier = Modifier
            .width(width = width)
            .height(height = height)
    ) {
        drawRect(
            color = iconColor,
            topLeft = Offset(0f, (this.size.height * 0.65f)),
            size = Size(this.size.width * 0.25f, this.size.height * 0.35f)
        )
        drawRect(
            color = iconColor,
            topLeft = Offset(this.size.width * 0.25f, (this.size.height * 0.45f)),
            size = Size(this.size.width * 0.25f, this.size.height * 0.55f)
        )
        drawRect(
            color = iconColor,
            topLeft = Offset(this.size.width * 0.50f, (this.size.height * 0.25f)),
            size = Size(this.size.width * 0.25f, this.size.height * 0.75f)
        )
        drawRect(
            color = iconColor,
            topLeft = Offset(this.size.width * 0.75f, (0f)),
            size = Size(this.size.width * 0.25f, this.size.height * 1f)
        )

    }
}


@Composable
@Preview
fun CreditCardIcon_Preview() {
    MyApplicationTheme() {
        BottomNavigationBar(navController = rememberNavController())
    }
}


