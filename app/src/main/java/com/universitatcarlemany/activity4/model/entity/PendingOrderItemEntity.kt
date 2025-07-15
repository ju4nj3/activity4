package com.universitatcarlemany.activity4.model.entity

import androidx.room.Entity

@Entity(
    tableName = "pending_order_items",
    primaryKeys = ["instrumentId"]
)
data class PendingOrderItemEntity(
    val instrumentId: Int
)