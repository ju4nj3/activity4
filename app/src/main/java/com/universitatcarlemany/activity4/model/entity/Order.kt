package com.universitatcarlemany.activity4.model.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.universitatcarlemany.activity4.model.dto.OrderDTO

class Order(
    private val user: User,
    private var items: MutableList<Instrument> = mutableListOf(),
    private var status: OrderStatus = OrderStatus.IN_PROGRESS,
    private var category: Category? = null,
    private var paidDate: LocalDateTime? = null,
    private var deliveredDate: LocalDateTime? = null
) {
    private var id: Int = nextId++
    private var totalCost = 0.0

    companion object {
        private var nextId = 1
    }

    fun setId(orderId: Int) {
        if (orderId <= 0) {
            throw IllegalArgumentException("Order ID must be a positive number")
        }
        this.id = orderId
    }

    fun getId(): Int = id

    fun addItem(item: Instrument) {
        if (status != OrderStatus.IN_PROGRESS) {
            throw IllegalStateException("Order is not in progress")
        }

        items.add(item)
        totalCost += item.getPrice()
    }

    fun removeItem(item: Instrument) {
        items.remove(item)
        totalCost -= item.getPrice()
    }

    fun getTotalCost(): Double = totalCost

    fun getUser(): User = user

    fun getItems(): List<Instrument> = items

    fun getStatus(): OrderStatus = status

    fun setStatus(newStatus: OrderStatus) {
        status = newStatus
    }

    fun getPaidDate(): LocalDateTime? = paidDate

    fun setPaidDate(newPaidDate: LocalDateTime) {
        paidDate = newPaidDate
    }

    fun getDeliveredDate(): LocalDateTime? = deliveredDate

    fun setDeliveredDate(newDeliveredDate: LocalDateTime) {
        deliveredDate = newDeliveredDate
    }

    fun toDto() : OrderDTO {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        return OrderDTO(
            id = getId(),
            status = getStatus().toString(),
            items = getItems().map { it.getId() },
            totalCost = getTotalCost(),
            paidDate = getPaidDate()?.format(formatter) ?: "",
            deliveredDate = getDeliveredDate()?.format(formatter)
        )
    }
}