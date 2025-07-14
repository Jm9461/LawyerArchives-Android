package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemSessionBinding
import com.lawyer_archives.models.CourtSession

class SessionAdapter(
    private val onEditClick: (CourtSession) -> Unit,
    private val onDeleteClick: (CourtSession) -> Unit
) : ListAdapter<CourtSession, SessionAdapter.SessionViewHolder>(SessionDiffCallback()) {

    inner class SessionViewHolder(private val binding: ItemSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CourtSession) {
            binding.sessionTitle.text = item.caseTitle
            binding.sessionDate.text = "تاریخ: ${item.sessionDate}"
            binding.location.text = "محل: ${item.location}"

            binding.editButton.setOnClickListener {
                onEditClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSessionBinding.inflate(inflater, parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SessionDiffCallback : DiffUtil.ItemCallback<CourtSession>() {
        override fun areItemsTheSame(oldItem: CourtSession, newItem: CourtSession): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CourtSession, newItem: CourtSession): Boolean =
            oldItem == newItem
    }
}