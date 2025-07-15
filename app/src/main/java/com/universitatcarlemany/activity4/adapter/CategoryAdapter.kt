package com.universitatcarlemany.activity4.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.universitatcarlemany.activity4.model.entity.Category
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.controller.InstrumentsActivity
/*import com.universitatcarlemany.activity4.controller.InstrumentsActivity*/
import com.universitatcarlemany.activity4.model.entity.User

class CategoryAdapter(private val categories: List<Category>, private val user: User) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvImage: ImageView = view.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("CategoryAdapter", "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Log.d("CategoryAdapter", "onBindViewHolder called for position $position")

            val category = categories[position]

            holder.tvName.text = category.getName()
            holder.tvDescription.text = category.getDescription()

            Glide.with(holder.itemView.context).load(category.getImage()).into(holder.tvImage)

            holder.itemView.setOnClickListener {

                val context = holder.itemView.context
                val intent = Intent(context, InstrumentsActivity::class.java)

                intent.putExtra("user", user)
                intent.putExtra("category", category)

                context.startActivity(intent)
            }
        } catch (e: Exception) {
            Log.d("RestaurantAdapter", "Error in onBindViewHolder at position $position: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        Log.d("RestaurantAdapter", "getItemCount called, size: ${categories.size}")
        return categories.size
    }

}