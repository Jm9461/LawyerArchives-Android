package com.lawyer_archives.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditClientBinding
import com.lawyer_archives.models.Client
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class EditClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditClientBinding
    private lateinit var clientList: MutableList<Client>
    private var originalClientId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalClientId = intent.getStringExtra("clientId")
        clientList = XmlManager.loadClients(this).toMutableList() // Ensure it's mutable

        val originalClient = clientList.find { it.id == originalClientId }

        originalClient?.let {
            binding.editClientName.setText(it.name)
            binding.editClientPhone.setText(it.phone)
            binding.editClientEmail.setText(it.email)
            binding.editClientAddress.setText(it.address)
        } ?: run {
            Toast.makeText(this, "موکل برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Corrected button ID from 'updateClientButton' to 'saveClientButton'
        // based on activity_edit_client.xml
        binding.saveClientButton.setOnClickListener {
            val name = binding.editClientName.text.toString()
            val phone = binding.editClientPhone.text.toString()
            val email = binding.editClientEmail.text.toString()
            val address = binding.editClientAddress.text.toString()

            if (name.isBlank() || phone.isBlank() || email.isBlank() || address.isBlank()) {
                Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedClient = originalClient.copy(
                name = name,
                phone = phone,
                email = email,
                address = address
            )

            val index = clientList.indexOfFirst { it.id == originalClientId }
            if (index != -1) {
                clientList[index] = updatedClient
                XmlManager.saveClients(this, clientList)
                Toast.makeText(this, "موکل با موفقیت به‌روزرسانی شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "موکل برای به‌روزرسانی یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}