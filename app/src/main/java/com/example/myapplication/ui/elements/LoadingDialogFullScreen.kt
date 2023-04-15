package com.example.myapplication.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialogFull(){
    Dialog(onDismissRequest = {  }) {
        Box {
            CircularProgressIndicator()
        }
    }
}