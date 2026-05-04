package com.example.myproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var items: MutableList<CartItem>,
    private val context: Context,
    private val onItemRemoved: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cart_item_name)
        val price: TextView = view.findViewById(R.id.cart_item_price)
        val quantity: TextView = view.findViewById(R.id.cart_item_quantity)
        val removeBtn: Button = view.findViewById(R.id.cart_remove_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.price.text = "${item.price} ₽"
        holder.quantity.text = "x${item.quantity}"

        holder.removeBtn.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)
            onItemRemoved()
        }
    }
}