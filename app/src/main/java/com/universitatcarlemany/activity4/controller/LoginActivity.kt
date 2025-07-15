package com.universitatcarlemany.activity4.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.universitatcarlemany.activity4.R
import com.universitatcarlemany.activity4.database.AppDatabase
import com.universitatcarlemany.activity4.model.entity.LoggedInUser
import com.universitatcarlemany.activity4.model.entity.User
import com.universitatcarlemany.activity4.repository.UsersRepository
import kotlinx.coroutines.launch

public class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private var users: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        supportActionBar?.title = "Inicio de sesión"

        val usersRepository = UsersRepository(this)
        users = usersRepository.loadUsers()

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@LoginActivity)
            val loggedUser = db.loggedInUserDao().getLoggedInUser()

            if (loggedUser != null) {

                val user = users.find { it.getId() == loggedUser.id }

                if (user != null) {
                    val intent = Intent(this@LoginActivity, CategoriesActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
            }
            else {
                etEmail.setText("juanje@juanjesusmartinez.com")
                etPassword.setText("1234")
            }
        }

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val user = users.find { it.getEmail() == email && it.getPassword() == password }

            if (user != null) {

                Toast.makeText(this, "¡Bienvenido, ${user.getName()}!", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@LoginActivity)

                    val userToSave = LoggedInUser(
                        id = user.getId(),
                        email = user.getEmail(),
                        name = user.getName()
                    )

                    db.loggedInUserDao().clear()
                    db.loggedInUserDao().insert(userToSave)

                    val intent = Intent(this@LoginActivity, CategoriesActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Usuario/contraseña no válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}