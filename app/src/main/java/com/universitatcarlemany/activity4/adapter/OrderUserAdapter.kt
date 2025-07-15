package com.universitatcarlemany.activity4.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.controller.OrderDetailActivity
import com.universitatcarlemany.activity4.model.entity.*
import java.time.format.DateTimeFormatter

class OrderUserAdapter(private val context: Context, private val orders: List<Order>, private val user: User) : RecyclerView.Adapter<OrderUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvPaidDate: TextView = view.findViewById(R.id.tvPaidDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order = orders[position]

        val dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatedDate = order.getPaidDate()?.format(dtFormatter)

        if (formatedDate != null) {
            holder.tvPaidDate.text = "Fecha Pedido: $formatedDate"
        }

        holder.tvOrderId.text = "Identificador: " + order.getId().toString()
        holder.tvTotal.text = "Total: €${String.format("%.2f", order.getTotalCost())}"
        holder.tvStatus.text = "Estado: ${order.getStatus().toString()}"
        holder.tvStatus.setStatusTextColor(order.getStatus())

        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("order_id", order.getId())
            intent.putExtra("user", user)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = orders.size
}
