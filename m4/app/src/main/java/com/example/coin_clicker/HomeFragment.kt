package com.example.coin_clicker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.coin_clicker.MainActivity

class HomeFragment : Fragment() {

    private lateinit var tvCoins: TextView
    private lateinit var tvCPS: TextView
    private lateinit var mainActivity: MainActivity
    private lateinit var viewRoot: View

    private val handler = Handler(Looper.getMainLooper())
    private val autoClickRunnable = object : Runnable {
        override fun run() {
            if (isAdded && ::mainActivity.isInitialized && mainActivity.autoClickers > 0) {
                mainActivity.onAutoClick()
                updateUI()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mainActivity = requireActivity() as MainActivity

        tvCoins = view.findViewById(R.id.tvCoins)
        tvCPS = view.findViewById(R.id.tvCPS)
        viewRoot = view

        view.setOnClickListener {
            mainActivity.onClick()
            updateUI()
        }

        updateUI()

        return view
    }

    private fun updateUI() {
        if (isAdded && ::mainActivity.isInitialized) {
            tvCoins.text = mainActivity.getFormattedCoins()
            val cps = mainActivity.autoClickers * mainActivity.autoClickPower
            tvCPS.text = "$cps coins/sec"
        }
    }

    private fun startAutoClicker() {
        stopAutoClicker()
        if (mainActivity.autoClickers > 0) {
            handler.post(autoClickRunnable)
        }
    }

    private fun stopAutoClicker() {
        handler.removeCallbacks(autoClickRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoClicker()
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        stopAutoClicker()
    }
}