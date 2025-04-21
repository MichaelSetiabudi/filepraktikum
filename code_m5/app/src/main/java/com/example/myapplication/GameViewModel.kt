package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val round1MaxWave = 5
    private val _gameState = MutableLiveData<GameState>(GameState.PLAYING)
    val gameState: LiveData<GameState> = _gameState

    private val _troopsCount = MutableLiveData(10)
    val troopsCount: LiveData<Int> = _troopsCount

    private val _playerPosition = MutableLiveData(0)
    val playerPosition: LiveData<Int> = _playerPosition

    private val _gateGrid = MutableLiveData<Array<Array<Gate?>>>(Array(7) { Array(2) { null } })
    val gateGrid: LiveData<Array<Array<Gate?>>> = _gateGrid

    private val _currentRound = MutableLiveData(1)
    val currentRound: LiveData<Int> = _currentRound

    private val _currentWave = MutableLiveData(0)
    val currentWave: LiveData<Int> = _currentWave

    private val _bossHP = MutableLiveData(500)
    val bossHP: LiveData<Int> = _bossHP

    private val _cheatActive = MutableLiveData(false)

    private var isGameRunning = false

    private var waveInProgress = false

    fun startGame() {
        _troopsCount.value = 10
        _playerPosition.value = 0
        _currentWave.value = 0
        _currentRound.value = 1
        _bossHP.value = 500
        _cheatActive.value = false
        waveInProgress = false

        _gateGrid.value = Array(7) { Array(2) { null } }

        isGameRunning = true
        startGameLoop()
    }

    fun moveLeft() {
        if (_playerPosition.value == 1) {
            _playerPosition.value = 0
        }
    }

    fun moveRight() {
        if (_playerPosition.value == 0) {
            _playerPosition.value = 1
        }
    }

    fun toggleCheat() {
        _cheatActive.value = !(_cheatActive.value ?: false)
    }

    private fun generateGate(): Gate {
        val value = Random.nextInt(1, 31)
        val operators = if (_cheatActive.value == true) {
            listOf("+", "×")
        } else {
            listOf("+", "-", "×", "÷")
        }
        val operator = operators[Random.nextInt(operators.size)]

        return Gate(value, operator, -1)
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (isGameRunning) {
                if (!waveInProgress && !isRoundComplete()) {
                    startNewWave()
                }

                delay(500)
            }
        }
    }

    private fun startNewWave() {
        viewModelScope.launch {
            waveInProgress = true

            _currentWave.value = (_currentWave.value ?: 0) + 1

            _gateGrid.value = Array(7) { Array(2) { null } }

            val grid = Array(7) { Array<Gate?>(2) { null } }
            grid[0][0] = generateGate().copy(column = 0)
            grid[0][1] = generateGate().copy(column = 1)
            _gateGrid.value = grid

            for (row in 0 until 6) {
                delay(700)

                val updatedGrid = Array(7) { Array<Gate?>(2) { null } }
                updatedGrid[row + 1][0] = _gateGrid.value?.get(row)?.get(0)
                updatedGrid[row + 1][1] = _gateGrid.value?.get(row)?.get(1)
                _gateGrid.value = updatedGrid
            }

            checkPlayerCollision()

            _gateGrid.value = Array(7) { Array(2) { null } }
            waveInProgress = false

            if (isRoundComplete()) {
                isGameRunning = false
                _gameState.value = GameState.BOSS_FIGHT
            }
        }
    }

    private fun checkPlayerCollision() {
        val grid = _gateGrid.value ?: return
        val playerPos = _playerPosition.value ?: 0

        val gate = grid[6][playerPos]
        if (gate != null) {
            processTroopChange(gate)
        }
    }

    private fun processTroopChange(gate: Gate) {
        val currentTroops = _troopsCount.value ?: 10
        val gateValue = gate.value

        _troopsCount.value = when (gate.operator) {
            "+" -> currentTroops + gateValue
            "-" -> maxOf(1, currentTroops - gateValue)
            "×" -> currentTroops * gateValue
            "÷" -> maxOf(1, currentTroops / gateValue)
            else -> currentTroops
        }
    }
    fun getMaxWavesForRound(): Int {
        return when (_currentRound.value) {
            1 -> round1MaxWave
            2 -> 7
            3 -> 10
            else -> 10
        }
    }
    private fun isRoundComplete(): Boolean {
        val currentWave = _currentWave.value ?: 0
        return when (_currentRound.value) {
            1 -> currentWave >= round1MaxWave
            2 -> currentWave >= 7
            3 -> currentWave >= 10
            else -> false
        }
    }

    fun updateTroopsAfterBossFight(remainingTroops: Int) {
        _troopsCount.value = remainingTroops
    }

    fun continueToNextRound(nextRound: Int) {
        _currentRound.value = nextRound
        _bossHP.value = when (nextRound) {
            1 -> 500
            2 -> 1000
            3 -> 2000
            else -> 500
        }

        val previousWavesTotal = when (nextRound) {
            2 -> 5
            3 -> 7
            else -> 0
        }
        _currentWave.value = previousWavesTotal

        _gameState.value = GameState.PLAYING
        waveInProgress = false
        isGameRunning = true

        startGameLoop()
    }

    fun resetGame() {
        isGameRunning = false
        startGame()
        _gameState.value = GameState.PLAYING
    }

    override fun onCleared() {
        super.onCleared()
        isGameRunning = false
    }
}