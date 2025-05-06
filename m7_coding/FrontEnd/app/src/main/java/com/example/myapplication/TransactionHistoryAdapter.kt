package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionHistoryAdapter : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    private val transactions = mutableListOf<TransactionEntity>()

    fun updateTransactions(newTransactions: List<TransactionEntity>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_history, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        private val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val tvEmployeeName: TextView = itemView.findViewById(R.id.tvEmployeeName)
        private val llProductDetails: LinearLayout = itemView.findViewById(R.id.llProductDetails)
        private val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)

        fun bind(transaction: TransactionEntity) {
            tvTransactionId.text = "Transaction ID: ${transaction.transaction_id}"
            tvCustomerName.text = "Customer: ${transaction.customer_name}"
            tvEmployeeName.text = "Employee: ${transaction.cashier_name}"

            llProductDetails.removeAllViews()

            // Add each product detail
            for (detail in transaction.detail) {
                val detailView = TextView(itemView.context).apply {
                    text = detail
                    textSize = 14f
                    setPadding(0, 4, 0, 4)
                }
                llProductDetails.addView(detailView)
            }

            val formattedTotal = "Total: Rp. ${formatNumber(transaction.total)}"
            tvTotalPrice.text = formattedTotal
        }

        private fun formatNumber(number: Int): String {
            return number.toString().replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")
        }
    }
}