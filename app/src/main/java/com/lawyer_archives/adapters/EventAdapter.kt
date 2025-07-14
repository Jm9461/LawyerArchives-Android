package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.models.Meeting

// EventAdapter برای نمایش هر دو نوع CourtSession و Meeting در یک لیست
class EventAdapter : ListAdapter<Any, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEventType: TextView = itemView.findViewById(R.id.tvEventType)
        private val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        private val tvEventDetails: TextView = itemView.findViewById(R.id.tvEventDetails)

        fun bind(item: Any) {
            when (item) {
                is CourtSession -> {
                    tvEventType.text = "جلسه دادرسی"
                    tvEventTitle.text = item.title
                    tvEventDetails.text = "تاریخ: ${item.courtDate}, شعبه: ${item.courtBranch}, ساعت: ${item.courtTime}"
                }
                is Meeting -> {
                    tvEventType.text = "قرار ملاقات"
                    tvEventTitle.text = item.title
                    tvEventDetails.text = "تاریخ: ${item.date}, با: ${item.clientName}, ساعت: ${item.time}"
                }
                else -> {
                    tvEventType.text = "رویداد نامشخص"
                    tvEventTitle.text = ""
                    tvEventDetails.text = ""
                }
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is CourtSession && newItem is CourtSession -> oldItem.id == newItem.id
                oldItem is Meeting && newItem is Meeting -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is CourtSession && newItem is CourtSession -> oldItem == newItem
                oldItem is Meeting && newItem is Meeting -> oldItem == newItem
                else -> false
            }
        }
    }
}