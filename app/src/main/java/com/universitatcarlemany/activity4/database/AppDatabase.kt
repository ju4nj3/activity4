package com.universitatcarlemany.activity4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.universitatcarlemany.activity4.model.entity.PendingOrderItemEntity
import com.universitatcarlemany.activity4.model.entity.LoggedInUser

@Database(
    entities = [PendingOrderItemEntity::class, LoggedInUser::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loggedInUserDao(): LoggedInUserDao
    abstract fun pendingOrder(): PendingOrderDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "musicstore"
                )
                .fallbackToDestructiveMigration(false)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}