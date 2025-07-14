package com.lawyer_archives.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.activities.AddClientActivity
import com.lawyer_archives.activities.EditLegalClientActivity
import com.lawyer_archives.activities.EditRealClientActivity
import com.lawyer_archives.adapters.ClientAdapter
import com.lawyer_archives.databinding.FragmentClientsBinding
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient
import com.lawyer_archives.utils.XmlManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private lateinit var clientList: MutableList<ClientEntry> // Changed type to ClientEntry
    private lateinit var adapter: ClientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadClients()

        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(requireContext(), AddClientActivity::class.java))
        }
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and the ClientAdapter.
     * Defines click listeners for edit and delete actions.
     */
    private fun setupRecyclerView() {
        binding.recyclerViewClients.layoutManager = LinearLayoutManager(requireContext())
        adapter = ClientAdapter(
            onEditClick = { clientEntry -> // Callback receives ClientEntry
                // Determine which edit activity to launch based on client type
                val intent: Intent = when (clientEntry) {
                    is RealClient -> Intent(requireContext(), EditRealClientActivity::class.java)
                    is LegalClient -> Intent(requireContext(), EditLegalClientActivity::class.java)
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
        val realClients = XmlManager.loadRealClients(requireContext())
        // Load legal clients
        val legalClients = XmlManager.loadLegalClients(requireContext())

        // Combine both lists into clientList
        clientList = mutableListOf<ClientEntry>().apply {
            addAll(realClients)
            addAll(legalClients)
        }
        adapter.submitList(clientList.toList()) // Submit a copy to the adapter
        if (clientList.isEmpty()) {
            Toast.makeText(requireContext(), "هنوز موکلی ثبت نشده است.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Shows a confirmation dialog before deleting a client.
     * @param clientEntry The ClientEntry object to be deleted.
     */
    private fun confirmAndDeleteClient(clientEntry: ClientEntry) {
        val clientName = when (clientEntry) {
            is RealClient -> clientEntry.fullName
            is LegalClient -> clientEntry.companyName
            else -> "موکل" // Fallback name
        }
        AlertDialog.Builder(requireContext())
            .setTitle("حذف موکل")
            .setMessage("آیا از حذف موکل \"$clientName\" اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                deleteClient(clientEntry)
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
                    XmlManager.saveRealClients(requireContext(), updatedRealClients)
                }
                is LegalClient -> {
                    val updatedLegalClients = clientList.filterIsInstance<LegalClient>()
                    XmlManager.saveLegalClients(requireContext(), updatedLegalClients)
                }
            }
            Toast.makeText(requireContext(), "موکل با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "خطا در حذف موکل.", Toast.LENGTH_SHORT).show()
        }
        loadClients() // Reload clients after deletion to refresh the list
    }

    override fun onResume() {
        super.onResume()
        loadClients() // Refresh list when returning from Add/Edit activities
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
