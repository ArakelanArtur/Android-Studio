package com.example.myproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val items: List<MenuItem>,
    private val context: Context,
    private val onAddToCart: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.menu_item_name)
        val description: TextView = view.findViewById(R.id.menu_item_desc)
        val price: TextView = view.findViewById(R.id.menu_item_price)
        val addBtn: Button = view.findViewById(R.id.menu_add_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.price.text = "${item.price} ₽"
        holder.addBtn.setOnClickListener {
            onAddToCart(item)
        }
    }
}