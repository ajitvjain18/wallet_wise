package com.techmania.expensemanager.Views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.techmania.expensemanager.databinding.LayoutLoginBinding
import com.techmania.expensemanager.databinding.LayoutNewLoginBinding

class LoginActivity : AppCompatActivity(){
    private lateinit var binding : LayoutNewLoginBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutNewLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        checkSecurity()
        initClicks()

    }

    private fun checkSecurity() {
        val isSecured = sharedPreferences.getBoolean("secured",false)
        if (isSecured==true) {

            binding.loginButton.isVisible = true
            binding.saveButton.isVisible = false

            val name = sharedPreferences.getString("name","")
            val email = sharedPreferences.getString("email","")
            val password = sharedPreferences.getString("password","")
            binding.tvname.setText(name)
            binding.tvemail.setText(email)
            binding.tlPassword.hint = "ENTER YOUR PASSCODE"



            binding.loginButton.setOnClickListener {
                val enteredPassword = binding.tvpassword.text.toString()
                if (password == enteredPassword)
                {
                    redirectToMainActivity()
                }
                else{
                    Toast.makeText(applicationContext,"Incorrect Passcode",Toast.LENGTH_SHORT).show()
                }
            }


        } else if (isLoggedIn()) {
            redirectToMainActivity()
            return
        }
    }

    private fun redirectToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initClicks() {
        with(binding) {
            saveButton.setOnClickListener {
                if (areAllFieldsFilled(tvname,tvemail,tvpassword)){
                    val name = tvname.text.toString()
                    val email = tvemail.text.toString()
                    val pwd = tvpassword.text.toString()
                    saveUserData(name,email,pwd)
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(applicationContext, "Please enter all details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserData(name:String,email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }


    fun areAllFieldsFilled(vararg fields: TextInputEditText): Boolean {
        for (field in fields) {
            if (field.text.isNullOrBlank()) {
                return false
            }
        }
        return true
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}