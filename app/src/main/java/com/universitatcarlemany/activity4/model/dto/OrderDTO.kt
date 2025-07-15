package com.universitatcarlemany.activity4.model.dto

data class OrderDTO(
    val id: Int,
    val status: String,
    val items: List<Int>,
    val totalCost: Double,
    val paidDate: String?,
    val deliveredDate: String?
)