package com.example.coin_clicker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    var totalCoins: Long = 0
    var clickPower: Long = 1
    var autoClickers: Long = 0
    var autoClickPower: Long = 1
    var prestigePoints: Long = 0
    var bonusMultiplier: Double = 1.0

    private var clickPowerLevel: Int = 0
    private var autoClickerLevel: Int = 0
    private var autoClickPowerLevel: Int = 0

    private val CLICK_POWER_BASE_COST: Long = 10
    private val CLICK_POWER_SCALING: Double = 1.8

    private val AUTO_CLICKER_BASE_COST: Long = 50
    private val AUTO_CLICKER_SCALING: Double = 2.1

    private val AUTO_CLICK_POWER_BASE_COST: Long = 100
    private val AUTO_CLICK_POWER_SCALING: Double = 2.4

    val clickPowerCost: Long
        get() = (CLICK_POWER_BASE_COST * CLICK_POWER_SCALING.pow(clickPowerLevel)).toLong()

    val autoClickerCost: Long
        get() = (AUTO_CLICKER_BASE_COST * AUTO_CLICKER_SCALING.pow(autoClickerLevel)).toLong()

    val autoClickPowerCost: Long
        get() = (AUTO_CLICK_POWER_BASE_COST * AUTO_CLICK_POWER_SCALING.pow(autoClickPowerLevel)).toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottom_nav_view).setupWithNavController(navController)
    }

    fun onClick() {
        totalCoins += (clickPower * bonusMultiplier).toLong()
    }

    fun onAutoClick() {
        val coinsToAdd = (autoClickers * autoClickPower * bonusMultiplier).toLong()
        totalCoins += coinsToAdd
        println("Auto-click added $coinsToAdd coins. Total coins: $totalCoins")
    }

    fun upgradeClickPower(): Boolean {
        val cost = clickPowerCost
        if (totalCoins >= cost) {
            totalCoins -= cost
            clickPower += 1
            clickPowerLevel += 1
            return true
        }
        return false
    }

    fun buyAutoClicker(): Boolean {
        val cost = autoClickerCost
        if (totalCoins >= cost) {
            totalCoins -= cost
            autoClickers += 1
            autoClickerLevel += 1
            return true
        }
        return false
    }

    fun upgradeAutoClickPower(): Boolean {
        val cost = autoClickPowerCost
        if (totalCoins >= cost) {
            totalCoins -= cost
            autoClickPower += 1
            autoClickPowerLevel += 1
            return true
        }
        return false
    }

    fun resetGame(): Long {
        val newPrestigePoints = totalCoins / 1_000_000_000
        prestigePoints += newPrestigePoints

        bonusMultiplier = 1.0 + (prestigePoints * 0.005)

        totalCoins = 0
        clickPower = 1
        autoClickers = 0
        autoClickPower = 1

        clickPowerLevel = 0
        autoClickerLevel = 0
        autoClickPowerLevel = 0

        return newPrestigePoints
    }

    fun addCheatCoins(amount: Long) {
        totalCoins += amount
    }

    fun formatNumber(value: Long): String {
        return when {
            value >= 1_000_000_000_000_000_000 -> String.format("%.2f", value / 1_000_000_000_000_000_000.0) + "Qi"
            value >= 1_000_000_000_000_000 -> String.format("%.2f", value / 1_000_000_000_000_000.0) + "Qa"
            value >= 1_000_000_000_000 -> String.format("%.2f", value / 1_000_000_000_000.0) + "T"
            value >= 1_000_000_000 -> String.format("%.2f", value / 1_000_000_000.0) + "B"
            value >= 1_000_000 -> String.format("%.2f", value / 1_000_000.0) + "M"
            value >= 1_000 -> String.format("%.2f", value / 1_000.0) + "K"
            else -> "$value"
        }
    }

    fun getFormattedCoins(): String = formatNumber(totalCoins)
    fun getFormattedClickPowerCost(): String = formatNumber(clickPowerCost)
    fun getFormattedAutoClickerCost(): String = formatNumber(autoClickerCost)
    fun getFormattedAutoClickPowerCost(): String = formatNumber(autoClickPowerCost)

}