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

    // Fungsi tambahan untuk menampilkan jumlah klik per detik untuk autoClicker
    fun getClicksPerSecond(): Int {
        return coinManager.autoClickers // 1 click per second per auto-clicker
    }
}