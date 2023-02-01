package com.example.myapplication.ui.theme

import androidx.compose.runtime.compositionLocalOf

data class BottomBarVisibility(
    var visible: Boolean
)


val LocalBottomBarVisibility = compositionLocalOf { BottomBarVisibility(true) }