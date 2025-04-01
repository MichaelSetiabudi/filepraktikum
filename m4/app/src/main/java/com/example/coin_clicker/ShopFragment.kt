package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class ShopFragment : Fragment() {

    private lateinit var tvCurrentCoins: TextView
    private lateinit var btnGet1K: Button
    private lateinit var btnGet1M: Button
    private lateinit var btnGet1B: Button
    private lateinit var btnGet1T: Button
    private lateinit var btnGet1Qa: Button
    private lateinit var btnGet1Qi: Button

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        // Initialize views
        tvCurrentCoins = view.findViewById(R.id.tvCurrentCoins)
        btnGet1K = view.findViewById(R.id.btnGet1K)
        btnGet1M = view.findViewById(R.id.btnGet1M)
        btnGet1B = view.findViewById(R.id.btnGet1B)
        btnGet1T = view.findViewById(R.id.btnGet1T)
        btnGet1Qa = view.findViewById(R.id.btnGet1Qa)
        btnGet1Qi = view.findViewById(R.id.btnGet1Qi)

        // Update current coins display
        updateCoinsDisplay()

        // Set click listeners for all cheat buttons
        setupCheatButtons()

        return view
    }

    private fun setupCheatButtons() {
        btnGet1K.setOnClickListener {
            viewModel.addCoinsFromShop(1_000L) // 1K
            updateCoinsDisplay()
        }

        btnGet1M.setOnClickListener {
            viewModel.addCoinsFromShop(1_000_000L) // 1M
            updateCoinsDisplay()
        }

        btnGet1B.setOnClickListener {
            viewModel.addCoinsFromShop(1_000_000_000L) // 1B
            updateCoinsDisplay()
        }

        btnGet1T.setOnClickListener {
            viewModel.addCoinsFromShop(1_000_000_000_000L) // 1T
            updateCoinsDisplay()
        }

        btnGet1Qa.setOnClickListener {
            viewModel.addCoinsFromShop(1_000_000_000_000_000L) // 1Qa
            updateCoinsDisplay()
        }

        btnGet1Qi.setOnClickListener {
            viewModel.addCoinsFromShop(1_000_000_000_000_000_000L) // 1Qi - Be careful with Long max value
            updateCoinsDisplay()
        }
    }

    private fun updateCoinsDisplay() {
        tvCurrentCoins.text = "Coins: ${viewModel.getFormattedCoins()}"
    }

    override fun onResume() {
        super.onResume()
        updateCoinsDisplay()
    }
}