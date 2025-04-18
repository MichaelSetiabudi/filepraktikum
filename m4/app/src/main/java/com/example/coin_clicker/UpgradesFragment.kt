package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.coin_clicker.MainActivity

class UpgradesFragment : Fragment() {

    private lateinit var tvCoins: TextView
    private lateinit var tvClickPower: TextView
    private lateinit var tvClickPowerCost: TextView
    private lateinit var btnUpgradeClickPower: Button

    private lateinit var tvAutoClickers: TextView
    private lateinit var tvAutoClickersCost: TextView
    private lateinit var btnUpgradeAutoClicker: Button

    private lateinit var tvAutoClickPower: TextView
    private lateinit var tvAutoClickPowerCost: TextView
    private lateinit var btnUpgradeAutoClickPower: Button

    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upgrades, container, false)

        mainActivity = requireActivity() as MainActivity

        tvCoins = view.findViewById(R.id.tvCoins)
        tvClickPower = view.findViewById(R.id.tvClickPower)
        tvClickPowerCost = view.findViewById(R.id.tvClickPowerCost)
        btnUpgradeClickPower = view.findViewById(R.id.btnUpgradeClickPower)

        tvAutoClickers = view.findViewById(R.id.tvAutoClickers)
        tvAutoClickersCost = view.findViewById(R.id.tvAutoClickersCost)
        btnUpgradeAutoClicker = view.findViewById(R.id.btnUpgradeAutoClicker)

        tvAutoClickPower = view.findViewById(R.id.tvAutoClickPower)
        tvAutoClickPowerCost = view.findViewById(R.id.tvAutoClickPowerCost)
        btnUpgradeAutoClickPower = view.findViewById(R.id.btnUpgradeAutoClickPower)

        // Setup click listeners
        btnUpgradeClickPower.setOnClickListener {
            if (mainActivity.upgradeClickPower()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpgradeAutoClicker.setOnClickListener {
            if (mainActivity.buyAutoClicker()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpgradeAutoClickPower.setOnClickListener {
            if (mainActivity.upgradeAutoClickPower()) {
                updateUI()
            } else {
                Toast.makeText(context, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        }

        updateUI()

        return view
    }

    private fun updateUI() {
        tvCoins.text = "Coins: ${mainActivity.getFormattedCoins()}"

        tvClickPower.text = "Click Power: ${mainActivity.clickPower}"
        tvClickPowerCost.text = "Upgrade Cost: ${mainActivity.getFormattedClickPowerCost()}"

        tvAutoClickers.text = "Auto Clickers: ${mainActivity.autoClickers}"
        tvAutoClickersCost.text = "Upgrade Cost: ${mainActivity.getFormattedAutoClickerCost()}"

        tvAutoClickPower.text = "Auto Click Power: ${mainActivity.autoClickPower}"
        tvAutoClickPowerCost.text = "Upgrade Cost: ${mainActivity.getFormattedAutoClickPowerCost()}"
    }
}