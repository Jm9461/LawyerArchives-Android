// LawyerArchives/app/src/main/java/com/lawyer_archives/activities/AddLegalClientActivity.kt
package com.lawyer_archives.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lawyer_archives.databinding.ActivityAddLegalClientBinding
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.utils.DateConverter
import com.lawyer_archives.utils.XmlManager
import java.util.Calendar
import java.util.Date

class AddLegalClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLegalClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLegalClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etRegistrationDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSaveLegalClient.setOnClickListener { saveLegalClient() }
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
                binding.etRegistrationDate.setText(persianDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun saveLegalClient() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val registrationNumber = binding.etRegistrationNumber.text.toString().trim()
        val registrationDate = binding.etRegistrationDate.text.toString().trim()
        val legalNationalId = binding.etLegalNationalId.text.toString().trim()
        val economicCode = binding.etEconomicCode.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val legalRepresentativeName = binding.etLegalRepresentativeName.text.toString().trim()
        val representativeNationalId = binding.etRepresentativeNationalId.text.toString().trim()

        if (companyName.isEmpty() || legalNationalId.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "لطفاً فیلدهای اجباری (نام شرکت/سازمان، شناسه ملی، آدرس، تلفن) را پر کنید.", Toast.LENGTH_LONG).show()
            return
        }

        val newLegalClient = LegalClient(
            companyName = companyName,
            registrationNumber = registrationNumber,
            registrationDate = registrationDate,
            legalNationalId = legalNationalId,
            economicCode = economicCode,
            address = address,
            phone = phone,
            phoneNumber = mobile, // Mapped to phoneNumber in ClientEntry
            email = email,
            postalCode = postalCode,
            description = description,
            legalRepresentativeName = legalRepresentativeName,
            representativeNationalId = representativeNationalId,
            addedDate = DateConverter.convertGregorianToPersian(Date())
        )

        val legalClients = XmlManager.loadLegalClients(this).toMutableList()
        legalClients.add(newLegalClient)
        XmlManager.saveLegalClients(this, legalClients)

        Toast.makeText(this, "موکل حقوقی با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show()
        finish()
    }
}