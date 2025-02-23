package com.example.mynutrisionpal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynutrisionpal.R
import com.example.mynutrisionpal.db.User.UserDatabase
import com.example.mynutrisionpal.db.User.UserTable
import com.example.mynutrisionpal.databinding.ActivityRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = UserDatabase.getDatabase(this).userDao()

        //Set the Click on Sign in text
        binding.signintextview.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.registeButtonRegistrationFragment.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailAdressEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Validate input
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this,
                    getString(R.string.please_enter_a_valid_email_address), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this,
                    getString(R.string.password_must_be_at_least_6_characters), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val userExists = userDao.doesUserExist(email)
                withContext(Dispatchers.Main) {
                    if (userExists) {
                        Toast.makeText(this@RegistrationActivity,
                            getString(R.string.user_with_this_email_already_exists), Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser = UserTable(
                            fullName = fullName,
                            email = email,
                            password = password
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insertUser(newUser)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegistrationActivity,
                                    getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
