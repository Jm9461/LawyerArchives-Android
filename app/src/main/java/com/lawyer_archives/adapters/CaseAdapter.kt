// LawyerArchives/app/src/main/java/com/lawyer_archives/adapters/CaseAdapter.kt
// (Inside your existing CaseAdapter.kt file)

// ... existing imports ...
import android.widget.Button // Add this import

class CaseAdapter(
    private val onEditClick: (Case) -> Unit,
    private val onDeleteClick: (Case) -> Unit,
    private val onDocumentsClick: (Case) -> Unit // New callback for documents
) : ListAdapter<Case, CaseAdapter.CaseViewHolder>(CaseDiffCallback()) {

    // ... onCreateViewHolder and onBindViewHolder ...

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... existing views ...
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_case_title)
        private val tvClientName: TextView = itemView.findViewById(R.id.tv_case_client_name)
        private val tvCourtDate: TextView = itemView.findViewById(R.id.tv_case_court_date)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_case_status)
        private val btnEdit: Button = itemView.findViewById(R.id.btn_edit_case)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete_case)
        private val btnDocuments: Button = itemView.findViewById(R.id.btn_documents_case) // New button

        init {
            // ... existing click listeners ...
            btnEdit.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onEditClick(getItem(adapterPosition))
                }
            }
            btnDelete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(adapterPosition))
                }
            }
            btnDocuments.setOnClickListener { // New click listener
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDocumentsClick(getItem(adapterPosition))
                }
            }
        }

        fun bind(case: Case) {
            tvTitle.text = case.title
            tvClientName.text = "موکل: ${case.clientName}"
            tvCourtDate.text = "تاریخ دادگاه: ${case.courtDate}"
            tvStatus.text = "وضعیت: ${case.status}"
        }
    }

    // ... CaseDiffCallback ...
}