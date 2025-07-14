package com.lawyer_archives.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.ClientAdapter
import com.lawyer_archives.databinding.ActivityClientsBinding
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast

class ClientsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientsBinding
    private lateinit var adapter: ClientAdapter
    private lateinit var clientList: MutableList<ClientEntry> // Changed type to ClientEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadClients()

        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(this, AddClientActivity::class.java))
        }
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and the ClientAdapter.
     * Defines click listeners for edit and delete actions.
     */
    private fun setupRecyclerView() {
        binding.recyclerViewClients.layoutManager = LinearLayoutManager(this)
        adapter = ClientAdapter(
            onEditClick = { clientEntry -> // Callback receives ClientEntry
                // Determine which edit activity to launch based on client type
                val intent: Intent = when (clientEntry) {
                    is RealClient -> Intent(this, EditRealClientActivity::class.java)
                    is LegalClient -> Intent(this, EditLegalClientActivity::class.java)
                    else -> throw IllegalArgumentException("Unknown client type for editing")
                }
                intent.putExtra("clientId", clientEntry.id)
                startActivity(intent)
            },
            onDeleteClick = { clientEntry ->
                confirmAndDeleteClient(clientEntry)
            }
        )
        binding.recyclerViewClients.adapter = adapter
    }

    /**
     * Loads both RealClient and LegalClient lists from XmlManager and combines them.
     */
    private fun loadClients() {
        // Load real clients
        val realClients = XmlManager.loadRealClients(this)
        // Load legal clients
        val legalClients = XmlManager.loadLegalClients(this)

        // Combine both lists into clientList
        clientList = mutableListOf<ClientEntry>().apply {
            addAll(realClients)
            addAll(legalClients)
        }
        adapter.submitList(clientList.toList()) // Submit a copy to the adapter
        if (clientList.isEmpty()) {
            Toast.makeText(this, "هنوز موکلی ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Shows a confirmation dialog before deleting a client.
     * @param client The ClientEntry object to be deleted.
     */
    private fun confirmAndDeleteClient(client: ClientEntry) {
        val clientName = when (client) {
            is RealClient -> client.fullName
            is LegalClient -> client.companyName
            else -> "موکل"
        }
        AlertDialog.Builder(this)
            .setTitle("حذف موکل")
            .setMessage("آیا از حذف موکل \"$clientName\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteClient(client)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    /**
     * Deletes a client from the appropriate XML file based on its type.
     * @param clientEntry The ClientEntry object to be deleted.
     */
    private fun deleteClient(clientEntry: ClientEntry) {
        val initialSize = clientList.size
        // Remove from the local list
        clientList.remove(clientEntry)

        val isDeleted = initialSize > clientList.size

        if (isDeleted) {
            // Save the updated list to the correct XML file based on type
            when (clientEntry) {
                is RealClient -> {
                    val updatedRealClients = clientList.filterIsInstance<RealClient>()
                    XmlManager.saveRealClients(this, updatedRealClients)
                }
                is LegalClient -> {
                    val updatedLegalClients = clientList.filterIsInstance<LegalClient>()
                    XmlManager.saveLegalClients(this, updatedLegalClients)
                }
            }
            Toast.makeText(this, "موکل با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "خطا در حذف موکل.", Toast.LENGTH_SHORT).show()
        }
        loadClients() // Reload clients after deletion to refresh the list
    }

    override fun onResume() {
        super.onResume()
        loadClients() // Refresh list when returning from Add/Edit activities
    }
}
