package com.example.myapplication.data.models

sealed class CreditCardTypes(override val name: String): Type {
    object MasterCard: CreditCardTypes("MasterCard")
    object Visa: CreditCardTypes("Visa")

    companion object{
        val All = listOf<CreditCardTypes>(MasterCard, Visa)


        fun getClass(name: String): CreditCardTypes{
            return if(name == "MasterCard"){
                MasterCard
            }else{
                Visa
            }
        }
    }
}