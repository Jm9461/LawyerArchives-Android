package com.lawyer_archives.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivitySetupProfileBinding
import java.security.MessageDigest // برای هش کردن رمز عبور

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // این بررسی دیگر در اینجا نیاز نیست، SplashActivity این کار را انجام می‌دهد
        // و SetupProfileActivity فقط زمانی فراخوانی می‌شود که profileSetupDone=false باشد.

        binding.buttonSaveProfile.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val license = binding.editTextLicense.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim() // فرض می‌کنیم این فیلد را به layout اضافه می‌کنید

            if (name.isBlank() || license.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "رمز عبور و تکرار آن یکسان نیستند.", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) { // حداقل طول رمز عبور
                Toast.makeText(this, "رمز عبور باید حداقل ۶ کاراکتر باشد.", Toast.LENGTH_SHORT).show()
            }
            // می‌توانید پیچیدگی رمز عبور را اینجا بیشتر بررسی کنید (مثلاً شامل حروف بزرگ، کوچک، اعداد و نمادها باشد)
            else {
                saveProfileAndPassword(name, license, password)
            }
        }
    }

    private fun saveProfileAndPassword(name: String, license: String, password: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_name", name)
            putString("profile_license", license)
            // **نکته مهم: رمز عبور را هش کنید!**
            putString("password_hash", hashPassword(password)) // استفاده از تابع هش
            putBoolean("profile_setup_done", true) // فلگ برای نشان دادن تکمیل تنظیمات اولیه
            apply()
        }
        Toast.makeText(this, "پروفایل و رمز عبور با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()

        // بعد از تنظیم موفقیت‌آمیز، به MainActivity هدایت شوید (این اولین ورود است)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // SetupProfileActivity را از پشته حذف کنید
    }

    // تابع برای هش کردن رمز عبور با SHA-256
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}