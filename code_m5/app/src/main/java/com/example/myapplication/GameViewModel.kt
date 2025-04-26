package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val maxWaveRound1 = 5
    private val _gameState = MutableLiveData<String>(GameState.PLAYING)
    val gameState: LiveData<String> = _gameState

    private val _troops = MutableLiveData(10)
    val troops: LiveData<Int> = _troops

    private val _playerPos = MutableLiveData(0)
    val playerPos: LiveData<Int> = _playerPos

    private val _gates = MutableLiveData<Array<Array<Gate?>>>(Array(7) { Array(2) { null } })
    val gates: LiveData<Array<Array<Gate?>>> = _gates

    private val _round = MutableLiveData(1)
    val round: LiveData<Int> = _round

    private val _wave = MutableLiveData(0)
    val wave: LiveData<Int> = _wave

    private val _bossHealth = MutableLiveData(500)
    val bossHealth: LiveData<Int> = _bossHealth

    private val _cheatMode = MutableLiveData(false)

    private var isRunning = false
    private var waveActive = false

    fun startGame() {
        _troops.value = 10
        _playerPos.value = 0
        _wave.value = 0
        _round.value = 1
        _bossHealth.value = 500
        _cheatMode.value = false
        waveActive = false

        _gates.value = Array(7) { Array(2) { null } }

        isRunning = true
        startGameLoop()
    }

    fun moveLeft() {
        if (_playerPos.value == 1) {
            _playerPos.value = 0
        }
    }

    fun moveRight() {
        if (_playerPos.value == 0) {
            _playerPos.value = 1
        }
    }

    fun toggleCheat() {
        _cheatMode.value = !(_cheatMode.value ?: false)
    }

    private fun generateGate(): Gate {
        val value = Random.nextInt(1, 31)
        val operators = if (_cheatMode.value == true) {
            listOf("+", "×")
        } else {
            listOf("+", "-", "×", "÷")
        }
        val operator = operators[Random.nextInt(operators.size)]

        return Gate(value, operator, -1)
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (isRunning) {
                if (!waveActive && !isRoundComplete()) {
                    startNewWave()
                }

                delay(500)
            }
        }
    }

    private fun startNewWave() {
        viewModelScope.launch {
            waveActive = true

            _wave.value = (_wave.value ?: 0) + 1

            _gates.value = Array(7) { Array(2) { null } }

            val grid = Array(7) { Array<Gate?>(2) { null } }
            grid[0][0] = generateGate().copy(column = 0)
            grid[0][1] = generateGate().copy(column = 1)
            _gates.value = grid

            for (row in 0 until 6) {
                delay(700)

                val updatedGrid = Array(7) { Array<Gate?>(2) { null } }
                updatedGrid[row + 1][0] = _gates.value?.get(row)?.get(0)
                updatedGrid[row + 1][1] = _gates.value?.get(row)?.get(1)
                _gates.value = updatedGrid
            }

            checkPlayerCollision()

            _gates.value = Array(7) { Array(2) { null } }
            waveActive = false

            if (isRoundComplete()) {
                isRunning = false
                _gameState.value = GameState.BOSS_FIGHT
            }
        }
    }

    private fun checkPlayerCollision() {
        val grid = _gates.value ?: return
        val pos = _playerPos.value ?: 0

        val gate = grid[6][pos]
        if (gate != null) {
            processTroopChange(gate)
        }
    }

    private fun processTroopChange(gate: Gate) {
        val current = _troops.value ?: 10
        val value = gate.value

        _troops.value = when (gate.operator) {
            "+" -> current + value
            "-" -> maxOf(1, current - value)
            "×" -> current * value
            "÷" -> maxOf(1, current / value)
            else -> current
        }
    }

    fun getMaxWavesForRound(): Int {
        return when (_round.value) {
            1 -> maxWaveRound1
            2 -> 7
            3 -> 10
            else -> 10
        }
    }

    private fun isRoundComplete(): Boolean {
        val currentWave = _wave.value ?: 0
        return when (_round.value) {
            1 -> currentWave >= maxWaveRound1
            2 -> currentWave >= 7
            3 -> currentWave >= 10
            else -> false
        }
    }

    fun updateTroopsAfterBossFight(remaining: Int) {
        _troops.value = remaining
    }

    fun continueToNextRound(nextRound: Int) {
        _round.value = nextRound
        _bossHealth.value = when (nextRound) {
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
        _wave.value = previousWavesTotal

        _gameState.value = GameState.PLAYING
        waveActive = false
        isRunning = true

        startGameLoop()
    }

    fun resetGame() {
        isRunning = false
        startGame()
        _gameState.value = GameState.PLAYING
    }

    override fun onCleared() {
        super.onCleared()
        isRunning = false
    }
}