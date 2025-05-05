package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class InventoryFragment : Fragment() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var rvInventory: RecyclerView
    private lateinit var btnBackToDashboard: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvInventory = view.findViewById(R.id.rvInventory)
        btnBackToDashboard = view.findViewById(R.id.btnBackToDashboard)

        setupRecyclerView()

        loadProductData()

        btnBackToDashboard.setOnClickListener {
            findNavController().navigate(R.id.action_inventoryFragment_to_cashierDashboardFragment)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        rvInventory.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadProductData() {
        lifecycleScope.launch {
                val productList = RetrofitInstance.product.getAllProduct()

                productAdapter.updateData(productList)


        }
    }
}