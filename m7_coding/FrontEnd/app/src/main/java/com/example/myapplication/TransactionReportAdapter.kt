package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TransactionReportAdapter(
    private var transactions: List<TransactionEntity> = emptyList()
) : RecyclerView.Adapter<TransactionReportAdapter.TransactionViewHolder>() {

    private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvDetail: TextView = itemView.findViewById(R.id.tvTransactionDetail)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTransactionTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.tvTransactionId.text = "Transaction ID : ${transaction.transaction_id}"
        holder.tvCustomerName.text = "Customer: ${transaction.customer_name}"

        val details = transaction.detail.joinToString("\n")
        holder.tvDetail.text = details

        holder.tvTotal.text = "Total: ${formatRupiah.format(transaction.total)}"
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newTransactions: List<TransactionEntity>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}