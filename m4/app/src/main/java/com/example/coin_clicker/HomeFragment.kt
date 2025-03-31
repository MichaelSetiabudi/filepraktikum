package com.example.coin_clicker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {

    private lateinit var tvCoins: TextView
    private lateinit var tvCPS: TextView
    private lateinit var viewModel: GameViewModel
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var autoClickerRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        // Initialize views
        tvCoins = view.findViewById(R.id.tvCoins)
        tvCPS = view.findViewById(R.id.tvCPS)

        // Setup click listeners
        view.setOnClickListener {
            viewModel.onClick()
            updateUI()
        }

        // Setup auto clicker sesuai kriteria
        autoClickerRunnable = object : Runnable {
            override fun run() {
                if (isAdded) {
                    viewModel.onAutoClick()
                    updateUI()
                    // Always use postDelayed to schedule next execution
                    handler.postDelayed(this, 1000) // Run every second
                }
            }
        }

        updateUI()
        startAutoClicker()

        return view
    }

    private fun updateUI() {
        if (isAdded) {
            tvCoins.text = viewModel.getFormattedCoins()
            tvCPS.text = "${viewModel.getFormattedCoinsPerSecond()} coins/sec"
        }
    }

    private fun startAutoClicker() {
        handler.postDelayed(autoClickerRunnable, 1000)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoClickerRunnable)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        startAutoClicker()
    }
}