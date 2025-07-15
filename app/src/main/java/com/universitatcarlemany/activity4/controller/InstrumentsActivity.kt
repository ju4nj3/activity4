package com.universitatcarlemany.activity4.controller

import Instruments
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.adapter.InstrumentsAdapter

class InstrumentsActivity : ComponentActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruments)

        val category: Category? = intent.getParcelableExtra("category", Category::class.java)

        if (category == null) {
            Log.e("InstrumentsActivity", "Category is null")
            finish()
            return
        }

        val user: User? = intent.getParcelableExtra("user", User::class.java)

        if (user == null) {
            Log.e("UserActivity", "User is null")
            finish()
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.title = category.getName()

        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val adapter = InstrumentsAdapter(category.getInstruments(), user)
        recyclerView.adapter = adapter
    }
}