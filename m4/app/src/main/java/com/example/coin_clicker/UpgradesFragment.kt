package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class UpgradesFragment : Fragment() {

    private lateinit var btnClickPower: Button
    private lateinit var btnAutoClicker: Button
    private lateinit var btnAutoClickPower: Button
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upgrades, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        // Initialize views
        btnClickPower = view.findViewById(R.id.btnClickPower)
        btnAutoClicker = view.findViewById(R.id.btnAutoClicker)
        btnAutoClickPower = view.findViewById(R.id.btnAutoClickPower)

        // Setup click listeners
        btnClickPower.setOnClickListener {
            if (viewModel.upgradeClickPower()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        btnAutoClicker.setOnClickListener {
            if (viewModel.buyAutoClicker()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        btnAutoClickPower.setOnClickListener {
            if (viewModel.upgradeAutoClickPower()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        btnClickPower.text = "Upgrade Click Power (${viewModel.getFormattedClickPowerCost()})\n" +
                "Current: +${viewModel.getClickPower()} coins/click"

        val clicksInfo = if (viewModel.getAutoClickers() > 0) {
            "1 click/sec each (total: ${viewModel.getClicksPerSecond()} clicks/sec)"
        } else {
            "0 clicks/sec"
        }
        btnAutoClicker.text = "Buy Auto Clicker (${viewModel.getFormattedAutoClickerCost()})\n" +
                "Current: ${viewModel.getAutoClickers()} clickers - $clicksInfo"

        // Tampilkan informasi autoClickPower dan cost
        btnAutoClickPower.text = "Upgrade Auto Click Power (${viewModel.getFormattedAutoClickPowerCost()})\n" +
                "Current: +${viewModel.getAutoClickPower()} coins/auto-click"
    }
}