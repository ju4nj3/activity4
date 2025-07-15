package com.universitatcarlemany.activity4.repository

import android.content.Context
import android.util.Log
import com.universitatcarlemany.activity4.model.entity.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrdersRepository(private val context: Context) {

    fun loadOrders(user: User, categories: List<Category>) {
        try {
            val file = context.assets.open("orders.json")
            val reader = InputStreamReader(file)

            reader.use {
                val ordersJson = it.readText()
                val jsonObject = JSONObject(ordersJson)
                val ordersArray = jsonObject.getJSONArray("orders")

                for (i in 0 until ordersArray.length()) {
                    val orderJson = ordersArray.getJSONObject(i)

                    val userId = orderJson.getInt("userId")

                    if (userId != user.getId()) {
                        continue
                    }

                    val order = Order(user)
                    order.setId(orderJson.getInt("id"))

                    if (orderJson.has("items")) {
                        val itemIds = orderJson.getJSONArray("items")
                        for (j in 0 until itemIds.length()) {
                            val instrumentId = itemIds.getInt(j)
                            val instrument = findInstrumentById(categories, instrumentId)
                            if (instrument != null) {
                                order.addItem(instrument)
                            } else {
                                Log.w("OrdersRepository", "Instrument ID $instrumentId not found")
                            }
                        }
                    }

                    if (orderJson.has("status")) {
                        val status = OrderStatus.valueOf(orderJson.getString("status"))
                        order.setStatus(status)
                    }

                    if (orderJson.has("paidDate") && orderJson.getString("paidDate").isNotEmpty()) {
                        val formatter = DateTimeFormatter.ISO_DATE_TIME
                        val paidDate = LocalDateTime.parse(orderJson.getString("paidDate"), formatter)
                        order.setPaidDate(paidDate)
                    }

                    if (orderJson.has("deliveredDate") && orderJson.getString("deliveredDate").isNotEmpty()) {
                        val formatter = DateTimeFormatter.ISO_DATE_TIME
                        val deliveredDate = LocalDateTime.parse(orderJson.getString("deliveredDate"), formatter)
                        order.setStatus(OrderStatus.DELIVERED)
                        order.setDeliveredDate(deliveredDate)
                    }

                    user.addOrder(order)
                }
            }

        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error loading orders: ${e.message}", e)
        }

        Log.d("OrdersRepository", "Orders loaded and assigned")
    }

    private fun findInstrumentById(categories: List<Category>, id: Int): Instrument? {
        for (category in categories) {
            for (instrument in category.getInstruments()) {
                if (instrument.getId() == id) {
                    return instrument
                }
            }
        }
        return null
    }
}