// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/DocumentAdapter.kt
package com.lawyer_archives.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lawyer_archives.R
import com.lawyer_archives.models.Document

class DocumentAdapter(
    private val onOpenClick: (Document) -> Unit,
    private val onDeleteClick: (Document) -> Unit
) : ListAdapter<Document, DocumentAdapter.DocumentViewHolder>(DocumentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val currentDocument = getItem(position)
        holder.bind(currentDocument)
    }

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDocumentName: TextView = itemView.findViewById(R.id.tv_document_name)
        private val tvDocumentType: TextView = itemView.findViewById(R.id.tv_document_type)
        private val btnOpenDocument: View = itemView.findViewById(R.id.btn_open_document)
        private val btnDeleteDocument: View = itemView.findViewById(R.id.btn_delete_document)

        init {
            btnOpenDocument.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onOpenClick(getItem(adapterPosition))
                }
            }
            btnDeleteDocument.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(adapterPosition))
                }
            }
        }

        fun bind(document: Document) {
            tvDocumentName.text = document.name
            // Display a more readable type based on MIME type
            val typeText = when {
                document.mimeType.contains("pdf") -> "نوع: PDF"
                document.mimeType.contains("image") -> "نوع: عکس"
                else -> "نوع: فایل"
            }
            tvDocumentType.text = typeText
        }
    }

    private class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }
}