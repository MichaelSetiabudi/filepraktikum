package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class CashierDashboardFragment : Fragment() {

    private lateinit var tvCashierWelcome: TextView
    private lateinit var btnViewInventory: Button
    private lateinit var btnViewTransactionHistory: Button
    private lateinit var btnGoToSale: Button
    private lateinit var btnCashierLogout: Button

    private var employeeName: String = "Cashier"
    private var employeeId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cashier_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCashierWelcome = view.findViewById(R.id.tvWelcome)
        btnViewInventory = view.findViewById(R.id.btnViewInventory)
        btnViewTransactionHistory = view.findViewById(R.id.btnViewTransactionHistory)
        btnGoToSale = view.findViewById(R.id.btnGoToSale)
        btnCashierLogout = view.findViewById(R.id.btnLogout)

        employeeName = arguments?.getString("employeeName") ?: "Cashier"
        employeeId = arguments?.getString("employeeId") ?: ""

        tvCashierWelcome.text = "Welcome, $employeeName"

        btnViewInventory.setOnClickListener {
            val bundle = Bundle().apply {
                putString("employeeId", employeeId)
            }
            findNavController().navigate(R.id.action_cashierDashboardFragment_to_inventoryFragment, bundle)
        }

        btnViewTransactionHistory.setOnClickListener {
            val bundle = Bundle().apply {
                putString("employeeId", employeeId)
                putString("employeeName", employeeName)
            }
            findNavController().navigate(R.id.action_cashierDashboardFragment_to_transactionHistoryFragment, bundle)
        }

        btnGoToSale.setOnClickListener {
            val bundle = Bundle().apply {
                putString("employeeId", employeeId)
                putString("employeeName", employeeName)
            }
            findNavController().navigate(R.id.action_cashierDashboardFragment_to_saleFragment, bundle)
        }

        btnCashierLogout.setOnClickListener {
            findNavController().navigate(R.id.action_cashierDashboardFragment_to_loginFragment)
        }
    }
}