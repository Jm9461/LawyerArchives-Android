// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/AddRealClientActivity.kt
package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddRealClientBinding
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class AddRealClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRealClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRealClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etBirthDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveRealClient.setOnClickListener { saveRealClient() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val persianDate = DateConverter.convertGregorianToPersian(Date(selectedYear - 1900, selectedMonth, selectedDay))
                binding.etBirthDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun saveRealClient() {
        val fullName = binding.etFullName.text.toString().trim()
        val fatherName = binding.etFatherName.text.toString().trim()
        val idCardNumber = binding.etIdCardNumber.text.toString().trim()
        val nationalId = binding.etNationalId.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val birthPlace = binding.etBirthPlace.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val occupation = binding.etOccupation.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (fullName.isEmpty() || nationalId.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام و نام خانوادگی، شماره ملی، موبایل) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        val newRealClient = RealClient(
            fullName = fullName,
            fatherName = fatherName,
            idCardNumber = idCardNumber,
            nationalId = nationalId,
            birthDate = birthDate,
            birthPlace = birthPlace,
            address = address,
            phone = phone,
            phoneNumber = mobile, // Mapped to phoneNumber in ClientEntry
            occupation = occupation,
            email = email,
            postalCode = postalCode,
            description = description,
            addedDate = DateConverter.convertGregorianToPersian(Date())
        )

        val realClients = XmlManager.loadRealClients(this).toMutableList()
        realClients.add(newRealClient)
        XmlManager.saveRealClients(this, realClients)

        Toast.makeText(this, "موکل حقیقی با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}