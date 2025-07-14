// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/DailyTaskAdapter.kt
package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.DailyTask

class DailyTaskAdapter(
    private val onEditClick: (DailyTask) -> Unit,
    private val onDeleteClick: (DailyTask) -> Unit
) : ListAdapter<DailyTask, DailyTaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTaskTitle: TextView = itemView.findViewById(R.id.tv_task_title)
        private val tvTaskDescription: TextView = itemView.findViewById(R.id.tv_task_description)
        private val tvTaskDueDate: TextView = itemView.findViewById(R.id.tv_task_due_date)
        private val btnEditTask: View = itemView.findViewById(R.id.btn_edit_task)
        private val btnDeleteTask: View = itemView.findViewById(R.id.btn_delete_task)

        init {
            btnEditTask.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onEditClick(getItem(adapterPosition))
                }
            }
            btnDeleteTask.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(adapterPosition))
                }
            }
        }

        fun bind(task: DailyTask) {
            tvTaskTitle.text = task.title
            tvTaskDescription.text = task.description
            tvTaskDueDate.text = task.dueDate
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<DailyTask>() {
        override fun areItemsTheSame(oldItem: DailyTask, newItem: DailyTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DailyTask, newItem: DailyTask): Boolean {
            return oldItem == newItem
        }
    }
}