package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.elements.BottomNavigationBar
import com.example.myapplication.ui.navigation.NavigationRoutes
import com.example.myapplication.ui.navigation.NavigationView
import com.example.myapplication.ui.theme.BottomBarVisibility
import com.example.myapplication.ui.theme.LocalBottomBarVisibility
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val currentBackStack = navController.currentBackStackEntryAsState()
            //TODO change all hardcored colors into MaterialThemer
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(bottomBar = {
                    if (currentBackStack.value?.destination?.route in arrayOf(
                            NavigationRoutes.MainScreen.route,
                            NavigationRoutes.GraphsScreen.route
                        )
                    ) {
                        BottomNavigationBar(navController = navController)
                    }
                }) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {

                        NavigationView(navController = navController)

                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    MyApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavigationView(navController = navController)
        }
    }
}