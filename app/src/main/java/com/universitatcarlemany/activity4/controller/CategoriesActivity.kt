package com.universitatcarlemany.activity4.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.adapter.CategoryAdapter
import com.universitatcarlemany.activity4.database.AppDatabase
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.repository.CategoriesRepository
import kotlinx.coroutines.launch

class CategoriesActivity : ComponentActivity() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Categorías"

        val repository = CategoriesRepository(this)
        val categories = repository.loadCategories()

        user = intent.getParcelableExtra("user", User::class.java)!!

        val userIcon: ImageView = findViewById(R.id.user_icon)

        userIcon.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("categories", categories.toTypedArray())
            startActivity(intent)
        }

        val cartIcon: ImageView = findViewById(R.id.cart_icon)

        cartIcon.setOnClickListener {
            val intent = Intent(this, OrderSummaryActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CategoryAdapter(categories, user!!)
        recyclerView.adapter = adapter

        loadPendingOrder(categories)
    }

    private fun loadPendingOrder(categories: List<Category>) {

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@CategoriesActivity)
            val pendingItems = db.pendingOrder().getAll()

            if (pendingItems.isNotEmpty()) {
                val order = OrderManager.createOrder(user)

                for (pendingItem in pendingItems) {

                    val instrument = categories
                        .flatMap { it.getInstruments() }
                        .find { it.getId() == pendingItem.instrumentId }

                    if (instrument != null) {
                        OrderManager.addItemToOrder(order, instrument)
                    }
                }

                user.addOrder(order)
                Log.d("CategoriesActivity", "Pedido restaurado con ${order.getItems().size} items")
            }
        }
    }
}

