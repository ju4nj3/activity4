package com.universitatcarlemany.activity4.repository

import android.content.Context
import android.util.Log
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.model.entity.Instrument
import org.json.JSONObject
import java.io.InputStreamReader

class CategoriesRepository(private val context: Context) {

    fun loadCategories(): List<Category> {

        val categories = mutableListOf<Category>()

        try {

            val file = context.assets.open("data.json")
            val reader = InputStreamReader(file)

            reader.use {

                val categoriesJson = it.readText()
                val jsonObject = JSONObject(categoriesJson)
                val categoriesArray = jsonObject.getJSONArray("categories")

                for (i in 0  until categoriesArray.length()) {

                    val categoryJson = categoriesArray.getJSONObject(i)

                    val id = categoryJson.getInt("id")
                    val name = categoryJson.getString("name")
                    val description = categoryJson.getString("description")
                    val image = categoryJson.getString("image")

                    val instruments = mutableListOf<Instrument>()
                    val instrumentsArray = categoryJson.getJSONArray("instruments")

                    val category = Category(id, name, description, image, instruments)

                    for (j in 0 until instrumentsArray.length()) {
                        val instrumentJson = instrumentsArray.getJSONObject(j)

                        val instrument = Instrument(
                            id = instrumentJson.getInt("id"),
                            name = instrumentJson.getString("name"),
                            description = instrumentJson.getString("description"),
                            price = instrumentJson.getDouble("price"),
                            condition = instrumentJson.getString("condition"),
                            features = instrumentJson.getString("features"),
                            image = instrumentJson.getString("image"),
                            units = instrumentJson.getInt("units"),
                        )

                        instruments.add(instrument)
                    }

                    category.setInstruments(instruments)
                    categories.add(category)
                }
            }

        } catch (e: Exception) {
                Log.e("CategoriesRepository", "Error loading categories" + e.message, e)
        }

        Log.d("CategoriesRepository", "Loaded users: ${categories.size}")

        return categories
    }
}