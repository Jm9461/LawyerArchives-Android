package com.lawyer_archives.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // نیازی به setContentView نیست چون این اکتیویتی فقط برای تصمیم‌گیری و انتقال است.
        // می‌توانید یک طرح‌بندی (layout) ساده برای نمایش لوگو یا نام برنامه در زمان کوتاه بارگذاری استفاده کنید.
        // مثلاً: setContentView(R.layout.activity_splash)

        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isProfileSetupDone = sharedPref.getBoolean("profile_setup_done", false)

        val intent: Intent
        if (isProfileSetupDone) {
            // اگر پروفایل قبلاً تنظیم شده است، به صفحه ورود (LoginActivity) هدایت شود
            intent = Intent(this, LoginActivity::class.java)
        } else {
            // اگر پروفایل هنوز تنظیم نشده است، به صفحه تنظیم پروفایل (SetupProfileActivity) هدایت شود
            intent = Intent(this, SetupProfileActivity::class.java)
        }
        startActivity(intent)
        finish() // SplashActivity را از پشته فعالیت‌ها حذف کنید تا کاربر نتواند به آن بازگردد
    }
}