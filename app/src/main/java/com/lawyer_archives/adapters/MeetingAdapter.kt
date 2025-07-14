package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.databinding.ItemMeetingBinding
import com.lawyer_archives.models.Meeting

class MeetingAdapter(
    private val onEditClick: (Meeting) -> Unit,
    private val onDeleteClick: (Meeting) -> Unit
) : ListAdapter<Meeting, MeetingAdapter.MeetingViewHolder>(MeetingDiffCallback()) {

    inner class MeetingViewHolder(private val binding: ItemMeetingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Meeting) {
            binding.meetingTopic.text = item.topic
            binding.meetingDate.text = "تاریخ: ${item.meetingDate}"
            binding.meetingClientName.text = "موکل: ${item.clientName}"

            binding.editButton.setOnClickListener {
                onEditClick(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMeetingBinding.inflate(inflater, parent, false)
        return MeetingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeetingDiffCallback : DiffUtil.ItemCallback<Meeting>() {
        override fun areItemsTheSame(oldItem: Meeting, newItem: Meeting): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Meeting, newItem: Meeting): Boolean =
            oldItem == newItem
    }
}