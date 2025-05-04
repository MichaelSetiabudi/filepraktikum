package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AdminDashboardFragment : Fragment() {

    private lateinit var tvAdminWelcome: TextView
    private lateinit var btnViewEmployeeReports: Button
    private lateinit var btnViewSupplyHistory: Button
    private lateinit var btnAdminLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        tvAdminWelcome = view.findViewById(R.id.tvAdminWelcome)
        btnViewEmployeeReports = view.findViewById(R.id.btnViewEmployeeReports)
        btnViewSupplyHistory = view.findViewById(R.id.btnViewSupplyHistory)
        btnAdminLogout = view.findViewById(R.id.btnAdminLogout)

        // Get employee name from arguments
        val employeeName = arguments?.getString("employeeName") ?: "Admin"
        tvAdminWelcome.text = "Welcome, $employeeName"

        // Set click listeners
        btnViewEmployeeReports.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_employeeReportFragment)
        }

        btnViewSupplyHistory.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_supplyHistoryFragment)
        }

        btnAdminLogout.setOnClickListener {
            // Navigate back to login fragment
            findNavController().navigate(R.id.action_adminDashboardFragment_to_loginFragment)
        }
    }
}