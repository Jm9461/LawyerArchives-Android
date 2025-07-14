// LawyerArchives/app/src/main/java/com/lawyer_archives/fragments/DailyTasksFragment.kt
package com.lawyer_archives.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyer_archives.adapters.DailyTaskAdapter
import com.lawyer_archives.databinding.FragmentDailyTasksBinding
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.utils.XmlManager

class DailyTasksFragment : Fragment() {

    private var _binding: FragmentDailyTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<DailyTask>
    private lateinit var adapter: DailyTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadTasks()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDailyTasks.layoutManager = LinearLayoutManager(requireContext())
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
        binding.recyclerViewDailyTasks.adapter = adapter
    }

    private fun loadTasks() {
        taskList = XmlManager.loadDailyTasks(requireContext()).toMutableList()
        adapter.submitList(taskList.toList())
        if (taskList.isEmpty()) {
            Toast.makeText(requireContext(), "هیچ کار روزانه‌ای وجود ندارد.", Toast.LENGTH_SHORT).show()
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