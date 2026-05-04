
package com.example.myproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// Личный кабинет - здесь корзина, меню, покупки и история

class ProfileActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Находим все кнопки и элементы на экране
        val welcomeText: TextView = findViewById(R.id.welcome_text)
        val menuBtn: Button = findViewById(R.id.menu_button)
        val historyBtn: Button = findViewById(R.id.history_button)
        val buyBtn: Button = findViewById(R.id.buy_button)
        cartRecyclerView = findViewById(R.id.cart_recycler_view)

        // Показываем логин пользователя (который сохранили при входе)
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val login = sharedPref.getString("current_login", "Гость")
        welcomeText.text = "Добро пожаловать, $login!"

        // Загружаем сохранённую корзину
        loadCart()

        // Настраиваем список корзины
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems, this) {
            saveCart() // При удалении товара сохраняем изменения
            Toast.makeText(this, "Товар удалён", Toast.LENGTH_SHORT).show()
        }
        cartRecyclerView.adapter = cartAdapter

        // Кнопка "Меню" - переход к списку блюд
        menuBtn.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Кнопка "История" - переход к истории покупок
        historyBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // Кнопка "Купить" - покупка товаров из корзины
        buyBtn.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show()
            } else {
                buyItems()
            }
        }
    }

    // Функция покупки
    private fun buyItems() {
        // Получаем текущую дату и время
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Собираем названия товаров в одну строку с количеством
        val itemsList = cartItems.map { "${it.name} x${it.quantity}" }
        val itemsString = itemsList.joinToString(", ")

        // Считаем общую сумму
        val totalPrice = cartItems.sumOf { it.price * it.quantity }

        // Сохраняем в историю покупок (формат: дата|товары|сумма;)
        val purchaseRecord = "$currentDate|$itemsString|$totalPrice"

        val sharedPref = getSharedPreferences("purchase_history", Context.MODE_PRIVATE)
        val existingHistory = sharedPref.getString("history", "") ?: ""
        val newHistory = if (existingHistory.isEmpty()) purchaseRecord else "$existingHistory;$purchaseRecord"
        sharedPref.edit().putString("history", newHistory).apply()

        // Очищаем корзину
        cartItems.clear()
        saveCart()
        cartAdapter.notifyDataSetChanged()

        // Показываем сообщение об успешной покупке
        Toast.makeText(this, "Куплено на $totalPrice ₽", Toast.LENGTH_LONG).show()
    }

    // Сохраняем корзину в телефон
    private fun saveCart() {
        val ids = cartItems.joinToString(",") { "${it.id}:${it.quantity}" }
        getSharedPreferences("cart_prefs", MODE_PRIVATE).edit()
            .putString("cart_data", ids).apply()
    }

    // Загружаем корзину из телефона
    private fun loadCart() {
        val data = getSharedPreferences("cart_prefs", MODE_PRIVATE)
            .getString("cart_data", "") ?: ""
        if (data.isNotEmpty()) {
            cartItems.clear()
            data.split(",").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    val id = parts[0].toInt()
                    val qty = parts[1].toInt()
                    val menuItem = getMenuItemById(id)
                    if (menuItem != null) {
                        cartItems.add(CartItem(id, menuItem.name, menuItem.price, qty))
                    }
                }
            }
        }
    }

    // Найти блюдо по его ID
    private fun getMenuItemById(id: Int): MenuItem? {
        return menuItemsList.find { it.id == id }
    }

    // Список всех блюд в меню
    companion object {
        val menuItemsList = listOf(
            MenuItem(1, "Борщ", "Наваристый украинский борщ", 250),
            MenuItem(2, "Пельмени", "Домашние пельмени с говядиной", 320),
            MenuItem(3, "Цезарь", "Салат Цезарь с курицей", 280),
            MenuItem(4, "Стейк", "Мраморная говядина гриль", 650),
            MenuItem(5, "Чизкейк", "Нью-Йорк чизкейк", 210)
        )
    }
}