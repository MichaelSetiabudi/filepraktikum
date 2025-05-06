package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployeeReportFragment : Fragment() {

    private lateinit var spinnerEmployee: Spinner
    private lateinit var rvEmployeeReport: RecyclerView
    private lateinit var btnBackToAdminDashboard: Button
    private lateinit var tvNoTransactions: TextView

    private var employeeList = mutableListOf<EmployeeEntity>()
    private lateinit var transactionAdapter: TransactionReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employee_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerEmployee = view.findViewById(R.id.spinnerEmployee)
        rvEmployeeReport = view.findViewById(R.id.rvEmployeeReport)
        btnBackToAdminDashboard = view.findViewById(R.id.btnBackToAdminDashboard)
        tvNoTransactions = view.findViewById(R.id.tvNoTransactions)
        rvEmployeeReport.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionReportAdapter()
        rvEmployeeReport.adapter = transactionAdapter

        btnBackToAdminDashboard.setOnClickListener {
            findNavController().navigate(R.id.action_employeeReportFragment_to_adminDashboardFragment)
        }

        loadEmployees()
    }

    private fun loadEmployees() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val employees = RetrofitInstance.employee.getEmployee()

                withContext(Dispatchers.Main) {

                    employeeList.clear()
                    employeeList.addAll(employees)

                    if (employeeList.isEmpty()) {
                        showError("No employees found")
                    } else {
                        setupEmployeeSpinner()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Failed to load employees: ${e.message}")
                }
            }
        }
    }

    private fun setupEmployeeSpinner() {
        val employeeNames = employeeList.map { it.nama_karyawan }.toTypedArray()

        // Create adapter for spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            employeeNames
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerEmployee.adapter = adapter

        spinnerEmployee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected employee name
                val selectedEmployeeName = employeeList[position].nama_karyawan

                // Load transactions for the selected employee
                loadEmployeeTransactions(selectedEmployeeName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun loadEmployeeTransactions(employeeName: String) {
        tvNoTransactions.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val transactions = RetrofitInstance.transaction.getTransactionReport(employeeName)

                withContext(Dispatchers.Main) {
                    if (transactions.isEmpty()) {
                        tvNoTransactions.visibility = View.VISIBLE
                        transactionAdapter.updateData(emptyList())
                    } else {
                        tvNoTransactions.visibility = View.GONE
                        transactionAdapter.updateData(transactions)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Failed to load transactions: ${e.message}")
                    tvNoTransactions.visibility = View.VISIBLE
                    transactionAdapter.updateData(emptyList())
                }
            }
        }
    }


    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = EmployeeReportFragment()
    }
}