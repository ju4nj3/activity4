package com.universitatcarlemany.activity4.controller

import com.universitatcarlemany.activity4.model.dto.OrderDTO
import com.universitatcarlemany.activity4.model.entity.Instrument
import com.universitatcarlemany.activity4.model.entity.Order
import com.universitatcarlemany.activity4.model.entity.OrderStatus
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.repository.CategoriesRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OrderManager {

    private val orders = mutableListOf<Order>()

    fun createOrder(user: User): Order {
        val order = Order(
            user = user
        )
        orders.add(order)
        user.addOrder(order)
        return order
    }

    fun getOrder(user: User): Order? {
        return orders.find {
            it.getUser().getEmail() == user.getEmail() && it.getStatus() == OrderStatus.IN_PROGRESS
        }
    }

    fun addItemToOrder(order: Order, item: Instrument) {
        order.addItem(item)
    }

    suspend fun saveOrder(order: Order) {

        order.setStatus(OrderStatus.PAID)
        order.setPaidDate(LocalDateTime.now())
        orders.add(order)
    }

    fun getOrders(): List<Order> {
        return orders
    }

    private fun ordertoOrderDTO(order: Order): OrderDTO {

        val dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        return OrderDTO(
            id = order.getId(),
            status = order.getStatus().toString(),
            items = order.getItems().map { it.getId() },
            totalCost = order.getTotalCost(),
            paidDate = order.getPaidDate()?.format(dtFormatter),
            deliveredDate = order.getDeliveredDate()?.format(dtFormatter)
        )
    }

    fun clear(order: Order) {
        orders.remove(order)
    }
}
