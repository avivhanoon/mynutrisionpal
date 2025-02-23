package com.example.mynutrisionpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.db.User.UserDatabase
import com.example.mynutrisionpal.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = UserDatabase.getDatabase(this).userDao()
        // set click on login button
        binding.loginButtonLoginFragment.setOnClickListener {
            val email = binding.emailAdressEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            // Checking if the input is valid
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                    getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userDao.getUserByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (user != null && user.password == password) {
                            Toast.makeText(this@LoginActivity,
                                getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity,
                                getString(R.string.invalid_email_or_password), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
