package com.universitatcarlemany.activity4.model.entity

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    private val id: Int,
    private val email: String,
    private val password: String,
    private val name: String,
    private val address: String,
    private val city: String,
    private val phone: String,
    private val birthDate: LocalDate,
    private val image: String,
) : Parcelable {
    @IgnoredOnParcel
    private val orders = mutableListOf<Order>()
    @IgnoredOnParcel
    private var inProgressOrder: Order = Order(this)
    @IgnoredOnParcel
    private val paidOrders = mutableSetOf<Order>()
    @IgnoredOnParcel
    private val deliveredOrders = mutableSetOf<Order>()

    init {
        if (!isValidEmail(email)) {
            throw IllegalArgumentException("Invalid email format")
        }

        orders.add(inProgressOrder)
    }

    fun getId(): Int = id

    fun getEmail(): String = email

    fun getPassword(): String = password

    fun getName(): String = name

    fun getAddress(): String = address

    fun getCity(): String = city

    fun getPhone(): String = phone

    fun getBirthDate(): LocalDate = birthDate

    fun getImage(): String = image

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
    }

    fun addOrder(order: Order) {
        orders.add(order)

        when (order.getStatus()) {

            OrderStatus.IN_PROGRESS -> {
                orders.remove(inProgressOrder)
                inProgressOrder = order
            }

            OrderStatus.PAID -> paidOrders.add(order)
            OrderStatus.DELIVERED -> deliveredOrders.add(order)
        }
    }

    fun getAllOrders(): List<Order> = orders.toList()
    fun getPaidOrders(): Set<Order> = paidOrders
    fun getDeliveredOrders(): Set<Order> = deliveredOrders
    fun getInProgressOrder(): Order = inProgressOrder
}