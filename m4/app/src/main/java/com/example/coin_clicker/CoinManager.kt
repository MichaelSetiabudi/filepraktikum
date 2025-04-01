package com.example.coin_clicker

class CoinManager {

    var totalCoins: Long = 0
        private set

    var coinsPerSecond: Long = 0
        private set

    // Track coins gained from shop separately (for prestige calculation)
    private var coinsFromShop: Long = 0

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
        // Apply prestige bonus to all normal income (clicks and auto-clicks)
        totalCoins += (amount * prestigeBonus).toLong()
    }

    // Add coins from shop (cheat) - doesn't affect prestige calculations
    fun addCoinsFromShop(amount: Long) {
        totalCoins += amount
        coinsFromShop += amount // Track shop coins separately for prestige calculation
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
            clickPower += 1  // Tambah 1 setiap kali upgrade
            clickPowerCost = (clickPowerCost * 1.5).toLong()  // Naikkan cost
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
            autoClickPower += 1  // Tambah 1 setiap kali upgrade
            autoClickPowerCost = (autoClickPowerCost * 1.5).toLong()
            updateCoinsPerSecond()
            return true
        }
        return false
    }

    private fun updateCoinsPerSecond() {
        val clicksPerSecond = autoClickers // 1 click per second per auto-clicker
        // Apply prestige bonus to coins per second calculation
        coinsPerSecond = ((clicksPerSecond * autoClickPower) * prestigeBonus).toLong()
    }

    // Calculate potential prestige points (excluding shop coins)
    fun calculatePotentialPrestigePoints(): Int {
        // Only count legitimately earned coins toward prestige
        val legitimateCoins = totalCoins - coinsFromShop
        return (legitimateCoins / 1_000_000_000L).toInt()
    }

    fun resetForPrestige(): Int {
        // Calculate new prestige points based only on legitimate coins
        val legitimateCoins = totalCoins - coinsFromShop
        val newPrestigePoints = (legitimateCoins / 1_000_000_000L).toInt()

        // Only add prestige points if there are new ones to gain
        if (newPrestigePoints > 0) {
            prestigePoints += newPrestigePoints

            // Update prestige bonus: 0.5% (0.005) per prestige point
            prestigeBonus = 1.0 + (prestigePoints * 0.005)

            // Reset all game values
            totalCoins = 0L
            coinsFromShop = 0L
            coinsPerSecond = 0L
            clickPower = 1L
            autoClickers = 0
            autoClickPower = 1L
            clickPowerCost = 10L
            autoClickerCost = 50L
            autoClickPowerCost = 100L

            return newPrestigePoints
        }

        return 0
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