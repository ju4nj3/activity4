package com.universitatcarlemany.activity4.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.universitatcarlemany.activity4.model.entity.PendingOrderItemEntity

@Dao
interface PendingOrderDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: PendingOrderItemEntity)

    @Query("SELECT * FROM pending_order_items")
    suspend fun getAll(): List<PendingOrderItemEntity>

    @Query("DELETE FROM pending_order_items")
    suspend fun deleteAll()
}