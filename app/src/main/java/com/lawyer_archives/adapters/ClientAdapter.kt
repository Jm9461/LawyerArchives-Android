package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.ClientEntry
import com.lawyer_archives.models.LegalClient
import com.lawyer_archives.models.RealClient

class ClientAdapter(
    private val onEditClick: (ClientEntry) -> Unit,
    private val onDeleteClick: (ClientEntry) -> Unit
) : ListAdapter<ClientEntry, ClientAdapter.ClientViewHolder>(ClientDiffCallback()) {

    // Define view types
    private val VIEW_TYPE_REAL_CLIENT = 1
    private val VIEW_TYPE_LEGAL_CLIENT = 2

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RealClient -> VIEW_TYPE_REAL_CLIENT
            is LegalClient -> VIEW_TYPE_LEGAL_CLIENT
            else -> throw IllegalArgumentException("Unknown client type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        return when (viewType) {
            VIEW_TYPE_REAL_CLIENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_client_real, parent, false)
                RealClientViewHolder(view, onEditClick, onDeleteClick)
            }
            VIEW_TYPE_LEGAL_CLIENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_client_legal, parent, false)
                LegalClientViewHolder(view, onEditClick, onDeleteClick)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Abstract ViewHolder class to provide common functionality for all client types.
     */
    abstract class ClientViewHolder(itemView: View, onEditClick: (ClientEntry) -> Unit, onDeleteClick: (ClientEntry) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        // Common buttons for edit and delete (assuming they exist in both item_client_real and item_client_legal)
        protected val btnEdit: View = itemView.findViewById(R.id.editButton)
        protected val btnDelete: View = itemView.findViewById(R.id.deleteButton)

        init {
            btnEdit.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onEditClick(bindingAdapter?.getItem(adapterPosition) as ClientEntry)
                }
            }
            btnDelete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(bindingAdapter?.getItem(adapterPosition) as ClientEntry)
                }
            }
        }

        abstract fun bind(clientEntry: ClientEntry)
    }

    /**
     * ViewHolder for displaying RealClient data.
     */
    class RealClientViewHolder(itemView: View, onEditClick: (ClientEntry) -> Unit, onDeleteClick: (ClientEntry) -> Unit) :
        ClientViewHolder(itemView, onEditClick, onDeleteClick) {
        private val tvFullName: TextView = itemView.findViewById(R.id.tv_full_name)
        private val tvNationalId: TextView = itemView.findViewById(R.id.tv_national_id)
        private val tvPhoneNumber: TextView = itemView.findViewById(R.id.tv_phone_number)
        private val tvAddedDate: TextView = itemView.findViewById(R.id.tv_added_date)

        override fun bind(clientEntry: ClientEntry) {
            val realClient = clientEntry as RealClient
            tvFullName.text = "نام کامل: ${realClient.fullName}"
            tvNationalId.text = "شماره ملی: ${realClient.nationalId}"
            tvPhoneNumber.text = "موبایل: ${realClient.phoneNumber}"
            tvAddedDate.text = "تاریخ ثبت: ${realClient.addedDate}"
        }
    }

    /**
     * ViewHolder for displaying LegalClient data.
     */
    class LegalClientViewHolder(itemView: View, onEditClick: (ClientEntry) -> Unit, onDeleteClick: (ClientEntry) -> Unit) :
        ClientViewHolder(itemView, onEditClick, onDeleteClick) {
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tv_company_name)
        private val tvLegalNationalId: TextView = itemView.findViewById(R.id.tv_legal_national_id)
        private val tvPhone: TextView = itemView.findViewById(R.id.tv_phone) // This might be the office phone
        private val tvAddedDate: TextView = itemView.findViewById(R.id.tv_added_date)

        override fun bind(clientEntry: ClientEntry) {
            val legalClient = clientEntry as LegalClient
            tvCompanyName.text = "شرکت: ${legalClient.companyName}"
            tvLegalNationalId.text = "شناسه ملی: ${legalClient.legalNationalId}"
            tvPhone.text = "تلفن: ${legalClient.phone}" // Using 'phone' field for office phone
            tvAddedDate.text = "تاریخ ثبت: ${legalClient.addedDate}"
        }
    }

    private class ClientDiffCallback : DiffUtil.ItemCallback<ClientEntry>() {
        override fun areItemsTheSame(oldItem: ClientEntry, newItem: ClientEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClientEntry, newItem: ClientEntry): Boolean {
            return when {
                oldItem is RealClient && newItem is RealClient -> oldItem == newItem
                oldItem is LegalClient && newItem is LegalClient -> oldItem == newItem
                else -> false
            }
        }
    }
}
