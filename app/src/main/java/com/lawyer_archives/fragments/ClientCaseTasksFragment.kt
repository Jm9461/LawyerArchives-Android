// LawyerArchives/app/src/main/java/com/lawyer_archives/fragments/ClientCaseTasksFragment.kt
package com.lawyer_archives.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DailyTaskAdapter
import com.lawyer_archives.databinding.FragmentClientCaseTasksBinding
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.utils.XmlManager

class ClientCaseTasksFragment : Fragment() {

    private var _binding: FragmentClientCaseTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<DailyTask>
    private lateinit var adapter: DailyTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientCaseTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadTasks()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewClientCaseTasks.layoutManager = LinearLayoutManager(requireContext())
        adapter = DailyTaskAdapter(
            onEditClick = { task ->
                // TODO:  Start edit activity
                Toast.makeText(requireContext(), "Edit: ${task.title}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { task ->
                // TODO:  Confirm and delete task
                Toast.makeText(requireContext(), "Delete: ${task.title}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewClientCaseTasks.adapter = adapter
    }

    private fun loadTasks() {
        //  You might want to filter tasks here, loading only those related to Clients/Cases
        taskList = XmlManager.loadDailyTasks(requireContext()).toMutableList()
        adapter.submitList(taskList.toList())
        if (taskList.isEmpty()) {
            Toast.makeText(requireContext(), "هیچ کار مرتبط با موکل/پرونده وجود ندارد.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}