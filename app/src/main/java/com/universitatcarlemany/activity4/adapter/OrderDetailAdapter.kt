package com.universitatcarlemany.activity4.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.model.entity.Instrument

class OrderDetailAdapter(private val instruments: List<Instrument>) : RecyclerView.Adapter<OrderDetailAdapter.MenuViewHolder>() {

        class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
            val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            Log.d("OrderDetailAdapter", "Entered onCreateViewHolder of OrderDetailAdapter")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico, parent, false)
            return MenuViewHolder(view)
        }

        @SuppressLint("DefaultLocale")
        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

            try{
                Log.d("OrderDetailAdapter", "Entered onBindViewHolder of OrderDetailAdapter")

                val instrument = instruments[position]
                holder.tvName.text = instrument.getName()
                holder.tvPrice.text = String.format("%.2f€", instrument.getPrice())
                holder.tvDescription.text = instrument.getDescription()

                Glide.with(holder.itemView.context).load(instrument.getImage()).into(holder.ivImage)

            } catch (e: Exception) {
                Log.d("OrderDetailAdapter", "Error in onBindViewHolder at position $position: ${e.message}")
            }

        }

        override fun getItemCount() = instruments.size
}