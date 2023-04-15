package com.example.myapplication.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.myapplication.ui.screens.DataGraphScreen
import com.example.myapplication.ui.viewmodels.GraphsViewModel

fun NavGraphBuilder.spendingGraphNavGraph(
    navController: NavController,
    graphsViewModel: GraphsViewModel
) {
    navigation(startDestination = NavigationRoutes.GraphsScreen.route, route = GRAPH_ROUTE){
        composable(route = NavigationRoutes.GraphsScreen.route){
            DataGraphScreen(viewModel = graphsViewModel)
        }
    }
}