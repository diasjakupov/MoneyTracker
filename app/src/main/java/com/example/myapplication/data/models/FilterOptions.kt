package com.example.myapplication.data.models


sealed class FilterOptions: Type{
    class SORT_ASC(override val name: String = "Sort Ascending"): FilterOptions()
    class SORT_DESC(override val name: String = "Sort Descending"): FilterOptions()

}
