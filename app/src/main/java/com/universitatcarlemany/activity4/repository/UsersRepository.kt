package com.universitatcarlemany.activity4.repository

import android.content.Context
import android.util.Log
import com.universitatcarlemany.activity4.model.entity.User
import org.json.JSONObject
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalTime

class UsersRepository(private val context: Context) {

    fun loadUsers(): List<User> {

        val users = mutableListOf<User>()

        try {

            val file = context.assets.open("users.json")
            val reader = InputStreamReader(file)

            reader.use {

                val usersJson = it.readText()
                val jsonObject = JSONObject(usersJson)
                val usersArray = jsonObject.getJSONArray("users")

                for (i in 0  until usersArray.length()) {

                    val userJson = usersArray.getJSONObject(i)
                    val id = userJson.getInt("id")
                    val email = userJson.getString("email")
                    val password = userJson.getString("password")
                    val name = userJson.getString("name")
                    val address = userJson.getString("address")
                    val city = userJson.getString("city")
                    val phone = userJson.getString("phone")
                    val birthDate = LocalDate.parse(userJson.getString("birthDate"))
                    val image = userJson.getString("image")

                    val user = User(
                        id,
                        email,
                        password,
                        name,
                        address,
                        city,
                        phone,
                        birthDate,
                        image
                    )

                    users.add(user)
                }
            }

        } catch (e: Exception) {
            Log.e("UsersRepository", "Error loading users: " + e.message, e)
        }

        Log.d("UsersRepository", "Loaded users: ${users.size}")

        return users
    }
}