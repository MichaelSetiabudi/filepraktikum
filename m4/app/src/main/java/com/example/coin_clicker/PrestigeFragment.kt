package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class PrestigeFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var tvPrestigePoints: TextView
    private lateinit var tvCurrentCoins: TextView
    private lateinit var tvPotentialPrestige: TextView
    private lateinit var tvPrestigeBonus: TextView
    private lateinit var btnReset: Button
    private lateinit var tvCoinsDisplay: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prestige, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        // Initialize views
        tvPrestigePoints = view.findViewById(R.id.tvPrestigePoints)
        tvCurrentCoins = view.findViewById(R.id.tvCurrentCoins)
        tvPotentialPrestige = view.findViewById(R.id.tvPotentialPrestige)
        tvPrestigeBonus = view.findViewById(R.id.tvPrestigeBonus)
        btnReset = view.findViewById(R.id.btnReset)
        tvCoinsDisplay = view.findViewById(R.id.tvCoinsDisplay)

        // Update UI
        updateUI()

        // Set up button click listener
        btnReset.setOnClickListener {
            // Only perform prestige if there are potential points to gain
            val potentialPoints = viewModel.calculatePotentialPrestigePoints()
            if (potentialPoints > 0) {
                val newPoints = viewModel.resetForPrestige()
                // Navigate back to Home fragment after prestige
                findNavController().navigate(R.id.action_prestigeFragment_to_homeFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        // Update current coins display in top right
        tvCoinsDisplay.text = viewModel.getFormattedCoins()

        // Update prestige information
        tvPrestigePoints.text = "Current Prestige Points: ${viewModel.getPrestigePoints()}"
        tvCurrentCoins.text = "Your Coins: ${viewModel.getFormattedCoins()}"

        // Calculate potential prestige points (excluding coins from shop)
        val potentialPoints = viewModel.calculatePotentialPrestigePoints()
        tvPotentialPrestige.text = "Potential Prestige Points: $potentialPoints"

        // Display current bonus as percentage
        val currentBonus = viewModel.getPrestigeBonus()
        val formattedBonus = String.format("%.1f", (currentBonus - 1.0) * 100)
        tvPrestigeBonus.text = "Current Bonus: +${formattedBonus}% to all income"

        // Enable reset button only if there are potential points to gain
        btnReset.isEnabled = potentialPoints > 0
    }
}