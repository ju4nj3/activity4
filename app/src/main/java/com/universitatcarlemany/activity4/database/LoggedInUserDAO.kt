package com.universitatcarlemany.activity4.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.universitatcarlemany.activity4.model.entity.LoggedInUser

@Dao
interface LoggedInUserDao {

    @Insert
    suspend fun insert(user: LoggedInUser)

    @Query("SELECT * FROM logged_in_user LIMIT 1")
    suspend fun getLoggedInUser(): LoggedInUser?

    @Query("DELETE FROM logged_in_user")
    suspend fun clear()
}