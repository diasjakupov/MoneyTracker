package com.example.myapplication.data.models

sealed class GraphFilterOptions: Type{
    class Month(override val name: String = "Month"): GraphFilterOptions()
    class Week(override val name: String = "Week"): GraphFilterOptions()

}
