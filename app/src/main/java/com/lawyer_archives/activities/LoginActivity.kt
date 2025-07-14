package com.lawyer_archives.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityLoginBinding // فرض می‌کنیم این View Binding را ایجاد می‌کنید
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val enteredPassword = binding.editTextPassword.text.toString().trim()

            if (enteredPassword.isBlank()) {
                Toast.makeText(this, "لطفاً رمز عبور را وارد کنید.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val storedPasswordHash = sharedPref.getString("password_hash", null)

            if (storedPasswordHash == null) {
                // این حالت نباید رخ دهد اگر SplashActivity درست عمل کرده باشد،
                // اما برای اطمینان یک پیام خطا نمایش می‌دهیم.
                Toast.makeText(this, "رمز عبور تنظیم نشده است. لطفاً برنامه را مجدداً نصب کنید.", Toast.LENGTH_LONG).show()
                // می‌توانید کاربر را به SetupProfileActivity هم هدایت کنید در این حالت
                val intent = Intent(this, SetupProfileActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            val enteredPasswordHash = hashPassword(enteredPassword)

            if (enteredPasswordHash == storedPasswordHash) {
                Toast.makeText(this, "ورود موفقیت‌آمیز بود.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // LoginActivity را از پشته حذف کنید
            } else {
                Toast.makeText(this, "رمز عبور اشتباه است.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // تابع برای هش کردن رمز عبور (همان تابعی که در SetupProfileActivity استفاده شد)
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}