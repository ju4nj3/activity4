package com.universitatcarlemany.activity4.controller

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.adapter.CategoryAdapter
import com.universitatcarlemany.activity4.adapter.UserAdapter
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.repository.CategoriesRepository
import com.universitatcarlemany.activity4.repository.OrdersRepository

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val user: User? = intent.getParcelableExtra("user", User::class.java)

        if (user == null) {
            Log.e("UserActivity", "User is null")
            finish()
            return
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.title = user.getName()

        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val categoriesArray: Array<Category>? = intent.getParcelableArrayExtra("categories", Category::class.java)
        val categories: List<Category> = categoriesArray?.toList() ?: emptyList()

        val repository = OrdersRepository(this)
        val orders = repository.loadOrders(user, categories)
        val adapter = UserAdapter(user)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}