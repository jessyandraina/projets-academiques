package com.example.studywisely.model.local

sealed class PriorityType(val label: String) {
    data object Faible : PriorityType("Faible")
    data object Moyenne : PriorityType("Moyenne")
    data object Elevee : PriorityType("Elevee")
}
