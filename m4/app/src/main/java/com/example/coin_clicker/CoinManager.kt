package com.example.coin_clicker


class CoinManager {

    var totalCoins: Long = 1
        private set

    var coinsPerSecond: Long = 0
        private set

    // Upgrade Data
    var clickPower: Long = 1
        private set
    var autoClickers: Int = 0
        private set
    var autoClickPower: Long = 1
        private set

    var clickPowerCost: Long = 10
        private set
    var autoClickerCost: Long = 50
        private set
    var autoClickPowerCost: Long = 100
        private set

    var prestigePoints: Int = 0
        private set
    var prestigeBonus: Double = 1.0
        private set
   fun addCoins(amount: Long) {
        totalCoins += (amount * prestigeBonus).toLong()
    }

    fun subtractCoins(amount: Long): Boolean {
        if (totalCoins >= amount) {
            totalCoins -= amount
            return true
        }
        return false
    }

    fun upgradeClickPower(): Boolean {
        if (subtractCoins(clickPowerCost)) {
            clickPower = (clickPower * 1.2).toLong()
            clickPowerCost = (clickPowerCost * 1.5).toLong()
            return true
        }
        return false
    }

    fun buyAutoClicker(): Boolean {
        if (subtractCoins(autoClickerCost)) {
            autoClickers++
            autoClickerCost = (autoClickerCost * 1.5).toLong()
            updateCoinsPerSecond()
            return true
        }
        return false
    }

    fun upgradeAutoClickPower(): Boolean {
        if (subtractCoins(autoClickPowerCost)) {
            autoClickPower = (autoClickPower * 1.2).toLong()
            autoClickPowerCost = (autoClickPowerCost * 1.5).toLong()
            updateCoinsPerSecond()
            return true
        }
        return false
    }

    private fun updateCoinsPerSecond() {
        coinsPerSecond = ((autoClickers * autoClickPower) * prestigeBonus).toLong()
    }

    fun resetForPrestige(): Int {
        val newPrestigePoints = (totalCoins / 1_000_000_000L).toInt()
        prestigePoints += newPrestigePoints
        prestigeBonus = 1.0 + (prestigePoints * 0.005)

        totalCoins = 0L
        coinsPerSecond = 0L
        clickPower = 1L
        autoClickers = 0
        autoClickPower = 1L
        clickPowerCost = 10L
        autoClickerCost = 50L
        autoClickPowerCost = 100L

        return newPrestigePoints
    }

    fun formatCoins(coins: Long): String {
        return when {
            coins >= 1_000_000_000_000_000L -> "${coins / 1_000_000_000_000_000L}Qi"
            coins >= 1_000_000_000_000L -> "${coins / 1_000_000_000_000L}Qa"
            coins >= 1_000_000_000L -> "${coins / 1_000_000_000L}B"
            coins >= 1_000_000L -> "${coins / 1_000_000L}M"
            coins >= 1_000L -> "${coins / 1_000L}K"
            else -> "$coins"
        }
    }
}