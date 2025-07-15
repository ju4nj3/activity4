package com.universitatcarlemany.activity4.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logged_in_user")
data class LoggedInUser(
    @PrimaryKey val id: Int,
    val email: String,
    val name: String
)
