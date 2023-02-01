package com.example.myapplication.data.models

sealed class UIState{
    object Loading: UIState()
    object Success: UIState()
    class Error(error: String): UIState()
}
