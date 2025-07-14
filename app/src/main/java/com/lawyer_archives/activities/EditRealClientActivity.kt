package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityEditRealClientBinding
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class EditRealClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRealClientBinding
    private var originalClientId: String? = null
    private var currentRealClient: RealClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRealClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalClientId = intent.getStringExtra("clientId")
        if (originalClientId == null) {
            Toast.makeText(this, "شناسه موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadRealClientData()

        binding.etBirthDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveRealClient.setOnClickListener { updateRealClient() }
    }

    /**
     * Loads the existing real client data from XML and populates the form fields.
     */
    private fun loadRealClientData() {
        // Load all real clients and find the one to edit
        val realClients = XmlManager.loadRealClients(this)
        currentRealClient = realClients.find { it.id == originalClientId }

        currentRealClient?.let { client ->
            binding.etFullName.setText(client.fullName)
            binding.etFatherName.setText(client.fatherName)
            binding.etIdCardNumber.setText(client.idCardNumber)
            binding.etNationalId.setText(client.nationalId)
            binding.etBirthDate.setText(client.birthDate)
            binding.etBirthPlace.setText(client.birthPlace)
            binding.etAddress.setText(client.address)
            binding.etPhone.setText(client.phone)
            binding.etMobile.setText(client.phoneNumber) // phoneNumber in model, mobile in layout
            binding.etOccupation.setText(client.occupation)
            binding.etEmail.setText(client.email)
            binding.etPostalCode.setText(client.postalCode)
            binding.etDescription.setText(client.description)
        } ?: run {
            Toast.makeText(this, "موکل حقیقی برای ویرایش یافت نشد.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * Displays a date picker dialog for selecting the birth date.
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Note: GregorianDate constructor expects 0-indexed month for Java Date,
                // but for PersianCalendar library, use actual month number.
                // Assuming DateConverter handles internal conversion correctly.
                val gregorianDate = Date(selectedYear - 1900, selectedMonth, selectedDay)
                val persianDate = DateConverter.convertGregorianToPersian(gregorianDate)
                binding.etBirthDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    /**
     * Updates the real client data in the XML file.
     */
    private fun updateRealClient() {
        val fullName = binding.etFullName.text.toString().trim()
        val fatherName = binding.etFatherName.text.toString().trim()
        val idCardNumber = binding.etIdCardNumber.text.toString().trim()
        val nationalId = binding.etNationalId.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val birthPlace = binding.etBirthPlace.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim() // This maps to phoneNumber in model
        val occupation = binding.etOccupation.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        // Basic validation
        if (fullName.isEmpty() || nationalId.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام و نام خانوادگی، شماره ملی، موبایل) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        currentRealClient?.let { existingClient ->
            val updatedRealClient = existingClient.copy(
                fullName = fullName,
                fatherName = fatherName,
                idCardNumber = idCardNumber,
                nationalId = nationalId,
                birthDate = birthDate,
                birthPlace = birthPlace,
                address = address,
                phone = phone,
                phoneNumber = mobile,
                occupation = occupation,
                email = email,
                postalCode = postalCode,
                description = description
                // addedDate remains unchanged
            )

            val realClients = XmlManager.loadRealClients(this).toMutableList()
            val index = realClients.indexOfFirst { it.id == updatedRealClient.id }
            if (index != -1) {
                realClients[index] = updatedRealClient
                XmlManager.saveRealClients(this, realClients)
                Toast.makeText(this, "موکل حقیقی با موفقیت ویرایش شد.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "خطا در ویرایش موکل حقیقی: موکل یافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
