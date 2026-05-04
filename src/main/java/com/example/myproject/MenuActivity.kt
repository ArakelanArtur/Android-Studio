package com.example.myproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val recyclerView: RecyclerView = findViewById(R.id.menu_recycler_view)
        val backBtn: Button = findViewById(R.id.back_to_profile_btn)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MenuAdapter(ProfileActivity.menuItemsList, this) { menuItem ->
            addToCart(menuItem)
        }

        backBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun addToCart(item: MenuItem) {
        val sharedPref = getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        val currentData = sharedPref.getString("cart_data", "") ?: ""
        val itemsMap = mutableMapOf<Int, Int>()

        if (currentData.isNotEmpty()) {
            currentData.split(",").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    itemsMap[parts[0].toInt()] = parts[1].toInt()
                }
            }
        }

        val newQty = itemsMap.getOrDefault(item.id, 0) + 1
        itemsMap[item.id] = newQty

        val newData = itemsMap.map { "${it.key}:${it.value}" }.joinToString(",")
        sharedPref.edit().putString("cart_data", newData).apply()

        Toast.makeText(this, "${item.name} добавлен в корзину", Toast.LENGTH_SHORT).show()
    }
}