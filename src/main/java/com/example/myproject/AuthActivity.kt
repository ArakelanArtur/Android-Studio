package com.example.myproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: Button = findViewById(R.id.button)
        val linkToReg: TextView = findViewById(R.id.link_to_ref)

        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DBHelper(this, null)
                val isAuth = db.getUser(login, password)

                if (isAuth) {
                    // Сохраняем логин пользователя
                    val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("current_login", login).apply()

                    Toast.makeText(this, "Добро пожаловать, $login!", Toast.LENGTH_LONG).show()

                    // Переход в личный кабинет
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)

                    // Очищаем поля
                    userLogin.text.clear()
                    userPassword.text.clear()
                } else {
                    Toast.makeText(this, "Пользователь '$login' не найден", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}