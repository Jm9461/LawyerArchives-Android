package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddClientBinding // مطمئن شوید این با layout شما سازگار است

class AddClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // فرض می‌کنیم در activity_add_client.xml دو دکمه دارید با IDهای btnAddRealClient و btnAddLegalClient
        binding.btnAddRealClient.setOnClickListener {
            startActivity(Intent(this, AddRealClientActivity::class.java))
            finish() // این Activity را ببندید زیرا کاربر به Activity مربوطه هدایت شده است
        }

        binding.btnAddLegalClient.setOnClickListener {
            startActivity(Intent(this, AddLegalClientActivity::class.java))
            finish() // این Activity را ببندید
        }
    }
}