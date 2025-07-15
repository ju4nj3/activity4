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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.universitatcarlemany.activity4.controller.OrderManager
import com.universitatcarlemany.activity4.model.entity.Instrument
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.controller.OrderManager
import com.universitatcarlemany.activity4.database.AppDatabase
import com.universitatcarlemany.activity4.model.entity.PendingOrderItemEntity
import kotlinx.coroutines.launch


class InstrumentsAdapter(private val instruments: List<Instrument>,
                         private val user: User

) : RecyclerView.Adapter<InstrumentsAdapter.MenuViewHolder>() {

        class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
            val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
            val tvUnits: TextView = itemView.findViewById(R.id.tvUnits)

            val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
            val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            Log.d("InstrumentsAdapter", "Entering onCreateViewHolder")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instrument, parent, false)
            return MenuViewHolder(view)
        }

        @SuppressLint("DefaultLocale", "SetTextI18n")
        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

            try{
                Log.d("MenuAdapter", "Entering onBindViewHolder")

                val instrument = instruments[position]

                holder.tvName.text = instrument.getName()
                holder.tvDescription.text = instrument.getDescription()
                holder.tvPrice.text = String.format("%.2f€", instrument.getPrice())
                holder.tvUnits.text = "Stock: "+ instrument.getUnits().toString()

                Glide.with(holder.itemView.context).load(instrument.getImage()).into(holder.ivImage)

                holder.btnAddToCart.setOnClickListener {
                    onMenuItemClick(holder.itemView.context, instrument, user)
                }

                holder.itemView.setOnClickListener {
                    onMenuItemClick(holder.itemView.context, instrument, user)
                }

            } catch (e: Exception) {
                Log.d("MenuAdapter", "Error in onBindViewHolder at position $position: ${e.message}")
            }

        }

        @SuppressLint("DefaultLocale")
        private fun onMenuItemClick(
            context: Context,
            instrument: Instrument,
            user: User
        ) {
            Log.d("MenuAdapter", "Entering handleMenuItemClick")
            if (instrument.getUnits() > 0){

                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                builder.setTitle("¿Agregar al pedido?")
                builder.setMessage("¿Agregar ${instrument.getName()} al pedido?\n\nPrecio: ${String.format("%.2f€", instrument.getPrice())}")

                builder.setPositiveButton("Añadir al carro") { dialog, _ ->

                    var order = OrderManager.getOrder(user)

                    if (order == null) {
                        order = OrderManager.createOrder(user)
                    }

                    try {
                        OrderManager.addItemToOrder(order, instrument)
                        Toast.makeText(context, "${instrument.getName()} se ha añadido al carro", Toast.LENGTH_SHORT).show()
                        instrument.decUnits()
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }

                    (context as? ComponentActivity)?.lifecycleScope?.launch {

                        val db = AppDatabase.getDatabase(context)

                        val itemToSave = PendingOrderItemEntity(
                            instrumentId = instrument.getId()
                        )

                        db.pendingOrder().insert(itemToSave)
                        Log.d("InstrumentsAdapter", "Item ${instrument.getId()} ${instrument.getName()} saved.")
                    }

                    dialog.dismiss()
                }

                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }

        override fun getItemCount() = instruments.size

}