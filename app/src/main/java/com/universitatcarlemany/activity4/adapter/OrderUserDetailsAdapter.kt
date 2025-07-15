package com.universitatcarlemany.activity4.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.controller.OrderSummaryActivity
import com.universitatcarlemany.activity4.model.entity.Instrument
import com.universitatcarlemany.activity4.model.entity.Order

class OrderUserDetailsAdapter(private val context: Context, private val instruments: List<Instrument>, private val order: Order) : RecyclerView.Adapter<OrderUserDetailsAdapter.MenuViewHolder>() {

        class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
            val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
            val removeFromCart: Button = itemView.findViewById(R.id.remove_from_cart)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            Log.d("OrderUserDetailsAdapter", "Entered onCreateViewHolder of OrderUserDetailsAdapter")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_summary_item, parent, false)
            return MenuViewHolder(view)
        }

        @SuppressLint("DefaultLocale", "NotifyDataSetChanged")
        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            try{
                Log.d("OrderUserDetailsAdapter", "Entered onBindViewHolder of OrderUserDetailsAdapter")
                val menuItem = instruments[position]
                holder.tvName.text = menuItem.getName()
                holder.tvPrice.text = String.format("%.2f€", menuItem.getPrice())
                holder.tvDescription.text = menuItem.getDescription()

                Glide.with(holder.itemView.context).load(menuItem.getImage()).into(holder.ivImage)

                holder.removeFromCart.setOnClickListener {
                    try {
                        order.removeItem(menuItem)

                        menuItem.addUnits(-1)

                        notifyDataSetChanged()
                        (context as? OrderSummaryActivity)?.reload()
                        Log.d("OrderDetailAdapter", "${menuItem.getName()} was removed from the order")
                    } catch (e: IllegalArgumentException) {
                        Log.e("OrderDetailAdapter", "Error while removing item from the order: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.d("OrderUserDetailsAdapter", "Error in onBindViewHolder at position $position: ${e.message}")
            }

        }

        override fun getItemCount() = instruments.size

}