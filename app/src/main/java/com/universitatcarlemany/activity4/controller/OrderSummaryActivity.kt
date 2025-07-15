package com.universitatcarlemany.activity4.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.adapter.OrderUserDetailsAdapter
import com.universitatcarlemany.activity4.database.AppDatabase
import com.universitatcarlemany.activity4.model.entity.User
import kotlinx.coroutines.launch
import android.app.PendingIntent
import android.content.Intent
import com.universitatcarlemany.activity4.model.entity.OrderStatus
import java.time.LocalDateTime

class OrderSummaryActivity : ComponentActivity() {

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        createNotificationChannel()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Resumen del Pedido"

        val user: User? = intent.getParcelableExtra("user", User::class.java)

        if (user == null) {
            Log.e("OrderSummaryActivity", "User is null")
            finish()
            return
        }

        val order = OrderManager.getOrder(user)

        if (order != null) {
            val recyclerView = findViewById<RecyclerView>(R.id.order_items_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val adapter = OrderUserDetailsAdapter(this, order.getItems(), order)
            recyclerView.adapter = adapter

            val totalTextView = findViewById<TextView>(R.id.order_total_text)
            totalTextView.text = "Total: €${String.format("%.2f", order.getTotalCost())}"

            val backButton: Button = findViewById(R.id.back_button)
            backButton.setOnClickListener {
                finish()
            }

            val finalizeOrderButton: Button = findViewById(R.id.finalize_order_button)

            if (order.getItems().isEmpty()) {
                finalizeOrderButton.visibility = View.GONE
                Toast.makeText(this, "Pedido sin artículos", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                finalizeOrderButton.visibility = View.VISIBLE
            }

            finalizeOrderButton.setOnClickListener {

                try {
                    lifecycleScope.launch {

                        order.setStatus(OrderStatus.PAID)
                        order.setPaidDate(LocalDateTime.now())
                        user.addOrder(order)

                        OrderManager.saveOrder(order)

                        val db = AppDatabase.getDatabase(this@OrderSummaryActivity)
                        db.pendingOrder().deleteAll()

                    }

                    // Preguntar permisos notificación

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            sendNotification(order)
                        } else {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                1
                            )
                            Toast.makeText(
                                this,
                                "Activa el permiso de notificaciones para ver la confirmación",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        sendNotification(order)
                    }

                    Toast.makeText(this, "Pedido guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish()

                } catch (e: Exception) {
                    Log.e("OrderSummaryActivity", "Error guardar pedido: " + e.message, e)
                }
            }

        } else {
            Toast.makeText(this, "No hay ningún pedido en progreso", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pedido"
            val descriptionText = "Notificaciones de pedidos"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("order_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(order: com.universitatcarlemany.activity4.model.entity.Order) {

        val intent = Intent(this, OrderSummaryActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "order_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Pedido pagado")
            .setContentText("Pedido pagado por el importe total de ${String.format("%.2f", order.getTotalCost())} €")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }
    }

    fun reload() {
        finish()
        startActivity(intent)
    }
}