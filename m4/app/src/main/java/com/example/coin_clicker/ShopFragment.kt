package com.example.coin_clicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.coin_clicker.MainActivity

class ShopFragment : Fragment() {

    private lateinit var tvTotalCoins: TextView
    private lateinit var btnAdd1K: Button
    private lateinit var btnAdd1M: Button
    private lateinit var btnAdd1B: Button
    private lateinit var btnAdd1T: Button
    private lateinit var btnAdd1Qa: Button
    private lateinit var btnAdd1Qi: Button
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        mainActivity = requireActivity() as MainActivity

        tvTotalCoins = view.findViewById(R.id.tvTotalCoins)
        btnAdd1K = view.findViewById(R.id.btnAdd1K)
        btnAdd1M = view.findViewById(R.id.btnAdd1M)
        btnAdd1B = view.findViewById(R.id.btnAdd1B)
        btnAdd1T = view.findViewById(R.id.btnAdd1T)
        btnAdd1Qa = view.findViewById(R.id.btnAdd1Qa)
        btnAdd1Qi = view.findViewById(R.id.btnAdd1Qi)

        updateTotalCoins()

        btnAdd1K.setOnClickListener {
            mainActivity.addCheatCoins(1_000)
            updateTotalCoins()
        }

        btnAdd1M.setOnClickListener {
            mainActivity.addCheatCoins(1_000_000)
            updateTotalCoins()
        }

        btnAdd1B.setOnClickListener {
            mainActivity.addCheatCoins(1_000_000_000)
            updateTotalCoins()
        }

        btnAdd1T.setOnClickListener {
            mainActivity.addCheatCoins(1_000_000_000_000)
            updateTotalCoins()
        }

        btnAdd1Qa.setOnClickListener {
            mainActivity.addCheatCoins(1_000_000_000_000_000)
            updateTotalCoins()
        }

        btnAdd1Qi.setOnClickListener {
            mainActivity.addCheatCoins(1_000_000_000_000_000_000)
            updateTotalCoins()
        }

        return view
    }

    private fun updateTotalCoins() {
        tvTotalCoins.text = "Coins: ${mainActivity.getFormattedCoins()}"
    }
}