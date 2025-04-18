package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.coin_clicker.MainActivity

class PrestigeFragment : Fragment() {

    private lateinit var tvPrestigePoints: TextView
    private lateinit var tvBonus: TextView
    private lateinit var btnReset: Button
    private lateinit var mainActivity: MainActivity
    private lateinit var tvCoins: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prestige, container, false)

        mainActivity = requireActivity() as MainActivity
        tvCoins = view.findViewById(R.id.tvCoins)
        tvPrestigePoints = view.findViewById(R.id.tvPrestigePoints)
        tvBonus = view.findViewById(R.id.tvBonus)
        btnReset = view.findViewById(R.id.btnReset)

        updateUI()

        btnReset.setOnClickListener {
            val prestigePointsGained = mainActivity.resetGame()
            Toast.makeText(context, "Reset complete! Gained $prestigePointsGained Prestige Points", Toast.LENGTH_SHORT).show()
            updateUI()
        }

        return view
    }

    private fun updateUI() {
        tvCoins.text = "Coins: ${mainActivity.getFormattedCoins()}"
        tvPrestigePoints.text = "Prestige Points: ${mainActivity.prestigePoints}"
        val bonusPercentage = mainActivity.prestigePoints * 0.5 // 0.5% per Prestige Point
        tvBonus.text = "Bonus: +${String.format("%.1f", bonusPercentage)}%"
    }
}