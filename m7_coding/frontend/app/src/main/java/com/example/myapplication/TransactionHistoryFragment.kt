package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionHistoryFragment : Fragment() {

    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var adapter: TransactionHistoryAdapter
    private lateinit var rvTransactionHistory: RecyclerView
    private lateinit var btnBackToDashboard: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoTransactions: TextView

    private var employeeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransactionHistoryViewModel::class.java)
        employeeId = arguments?.getString("employeeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        rvTransactionHistory = view.findViewById(R.id.rvTransactionHistory)
        btnBackToDashboard = view.findViewById(R.id.btnBackToDashboard)

        // Add progress bar and no transactions text
        progressBar = view.findViewById(R.id.progressBar)
        tvNoTransactions = view.findViewById(R.id.tvNoTransactions)

        // Set up recycler view
        adapter = TransactionHistoryAdapter()
        rvTransactionHistory.layoutManager = LinearLayoutManager(requireContext())
        rvTransactionHistory.adapter = adapter

        // Set up button listener
        btnBackToDashboard.setOnClickListener {
            findNavController().navigateUp()
        }

        // Observe viewmodel data
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                tvNoTransactions.visibility = View.VISIBLE
                rvTransactionHistory.visibility = View.GONE
            } else {
                tvNoTransactions.visibility = View.GONE
                rvTransactionHistory.visibility = View.VISIBLE
                adapter.updateTransactions(transactions)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        // Load data
        employeeId?.let { id ->
            viewModel.getTransactionHistory(id)
        } ?: run {
            Toast.makeText(requireContext(), "Employee ID not found", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(employeeId: String) =
            TransactionHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString("employeeId", employeeId)
                }
            }
    }
}