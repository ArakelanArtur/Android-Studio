package com.example.myproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// Экран "История покупок" - показывает всё, что я купил раньше

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val purchases = mutableListOf<PurchaseData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.history_recycler_view)
        val backBtn: Button = findViewById(R.id.back_to_profile_btn)
        val emptyText: TextView = findViewById(R.id.empty_history_text)

        loadHistory() // Загружаю сохранённые покупки

        if (purchases.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = MyAdapter()
        }

        backBtn.setOnClickListener { finish() }
    }

    // Загружаю историю из памяти телефона
    private fun loadHistory() {
        val saved = getSharedPreferences("purchase_history", MODE_PRIVATE)
            .getString("history", "") ?: ""

        purchases.clear()

        if (saved.isNotEmpty()) {
            // Формат: "дата|товары|сумма;дата|товары|сумма;..."
            saved.split(";").forEach { buy ->
                if (buy.isNotEmpty()) {
                    val parts = buy.split("|")
                    if (parts.size == 3) {
                        val date = parts[0]
                        val items = parts[1]
                        val total = parts[2].toIntOrNull() ?: 0
                        purchases.add(PurchaseData(date, items, total))
                    }
                }
            }
        }
    }

    // ========== АДАПТЕР (показывает каждую покупку) ==========
    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val dateTxt: TextView = view.findViewById(R.id.history_date)
            val itemsTxt: TextView = view.findViewById(R.id.history_items)
            val totalTxt: TextView = view.findViewById(R.id.history_total)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )

        override fun getItemCount() = purchases.size

        override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
            val p = purchases[pos]
            holder.dateTxt.text = p.date
            holder.itemsTxt.text = p.items
            holder.totalTxt.text = "${p.total} ₽"
        }
    }

    // Данные одной покупки
    data class PurchaseData(val date: String, val items: String, val total: Int)
}