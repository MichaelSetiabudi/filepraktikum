package com.example.coin_clicker

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val coinManager = CoinManager()

    // Click handler for main screen
    fun onClick() {
        coinManager.addCoins(coinManager.clickPower)
    }

    // Auto-click handler (called each second)
    fun onAutoClick() {
        if (coinManager.autoClickers > 0) {
            val clicksPerSecond = coinManager.autoClickers // 1 click per second per auto-clicker
            val coinsToAdd = clicksPerSecond * coinManager.autoClickPower
            coinManager.addCoins(coinsToAdd)
        }
    }

    // Add coins from shop (cheat)
    fun addCoinsFromShop(amount: Long) {
        coinManager.addCoinsFromShop(amount)
    }

    // Calculate potential prestige points (new method)
    fun calculatePotentialPrestigePoints(): Int {
        return coinManager.calculatePotentialPrestigePoints()
    }

    // Upgrade functions
    fun upgradeClickPower(): Boolean {
        return coinManager.upgradeClickPower()
    }

    fun buyAutoClicker(): Boolean {
        return coinManager.buyAutoClicker()
    }

    fun upgradeAutoClickPower(): Boolean {
        return coinManager.upgradeAutoClickPower()
    }

    // Prestige function
    fun resetForPrestige(): Int {
        return coinManager.resetForPrestige()
    }

    // Getters for UI
    fun getFormattedCoins(): String {
        return coinManager.formatCoins(coinManager.totalCoins)
    }

    fun getFormattedCoinsPerSecond(): String {
        return coinManager.formatCoins(coinManager.coinsPerSecond)
    }

    fun getFormattedClickPowerCost(): String {
        return coinManager.formatCoins(coinManager.clickPowerCost)
    }

    fun getFormattedAutoClickerCost(): String {
        return coinManager.formatCoins(coinManager.autoClickerCost)
    }

    fun getFormattedAutoClickPowerCost(): String {
        return coinManager.formatCoins(coinManager.autoClickPowerCost)
    }

    fun getClickPower(): Long {
        return coinManager.clickPower
    }

    fun getAutoClickers(): Int {
        return coinManager.autoClickers
    }

    fun getAutoClickPower(): Long {
        return coinManager.autoClickPower
    }

    fun getTotalCoins(): Long {
        return coinManager.totalCoins
    }

    // Get prestige information
    fun getPrestigePoints(): Int {
        return coinManager.prestigePoints
    }

    fun getPrestigeBonus(): Double {
        return coinManager.prestigeBonus
    }

    // Fungsi tambahan untuk menampilkan jumlah klik per detik untuk autoClicker
    fun getClicksPerSecond(): Int {
        return coinManager.autoClickers // 1 click per second per auto-clicker
    }
}